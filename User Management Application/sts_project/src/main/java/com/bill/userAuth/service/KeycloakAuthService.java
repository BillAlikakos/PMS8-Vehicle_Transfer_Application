package com.bill.userAuth.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.bill.userAuth.dataTypes.AuthRequest;
import com.bill.userAuth.dataTypes.AuthResponse;
import com.bill.userAuth.dataTypes.KeycloakRegistrationRequest;
import com.bill.userAuth.dataTypes.KeycloakRegistrationRequest.Credential;
import com.bill.userAuth.dataTypes.User;
import com.bill.userAuth.util.CommonMethods;
import com.bill.userAuth.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KeycloakAuthService
{
	@Value("${KEYCLOAK_URL}")
	private String KEYCLOAK_URL;
	@Value("${KEYCLOAK_ADMIN_URL}")
	private String KEYCLOAK_ADMIN_URL;
	@Value("${KEYCLOAK_USERS_URL}")
	private String KEYCLOAK_USERS_URL;
	@Value("${KEYCLOAK_TOKEN_URL}")
	private String KEYCLOAK_TOKEN_URL;
	@Value("${KEYCLOAK_TOKEN_CHECK_URL}")
	private  String KEYCLOAK_TOKEN_CHECK_URL;
	@Value("${ADMIN_CLIENT_SECRET}")
	private  String ADMIN_CLIENT_SECRET;
	
	private static String ADMIN_ACCESS_TOKEN = null;
	private Logger logger = LogManager.getLogger(KeycloakAuthService.class);
	
	@Autowired
	ObjectMapper om;
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param clientId
	 * @param clientSecret
	 * @param isAdmin
	 * @return
	 */
    public AuthResponse authenticateUser(String username, String password, String clientId, String clientSecret, boolean isAdmin) 
    {
        String authUrl = isAdmin ? KEYCLOAK_ADMIN_URL: KEYCLOAK_URL;
        authUrl += "protocol/openid-connect/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString());
        
        String requestBody = isAdmin ? "grant_type="+Constants.CLIENT_CREDENTIALS_GRANT+
                "&client_id="+ Constants.ADMIN_CLIENT +"&client_secret="+ ADMIN_CLIENT_SECRET
                : "grant_type="+Constants.PASSWORD_GRANT+"&username="+ username +"&password=" + password +
                 "&client_id="+ clientId +"&client_secret="+ clientSecret;
        
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = this.callKeycloak(authUrl, requestEntity, HttpMethod.POST);
        HttpStatusCode result = responseEntity.getStatusCode();
        this.logResponse(responseEntity, result);
        
        if(result.is2xxSuccessful())
        {
        	return retrieveTokens(responseEntity, isAdmin);
        }
        else
        {
        	logger.error(Constants.USER_AUTH_ERROR+username, Constants.ERROR_PREFIX+result);
        	return new AuthResponse(Constants.USER_AUTH_ERROR+username, Constants.ERROR_PREFIX+result);
        }
    }
    
    /**
     * 
     * @param request
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    private void signInToMasterRealm(AuthRequest request) throws JsonMappingException, JsonProcessingException
    {
    	if(ADMIN_ACCESS_TOKEN == null || !CommonMethods.checkTokenValidity(ADMIN_ACCESS_TOKEN))
    	{
    		logger.info("[signInToMasterRealm] Retrieving new admin access token");
        	AuthResponse resp = this.authenticateUser(Constants.ADMIN, "", request.getClientId(), request.getClientSecret(), true);
        	
        	if(resp == null || !resp.getMessage().equalsIgnoreCase(Constants.SUCCESS_MESSAGE))
        	{
        		logger.error(Constants.ADMIN_AUTH_ERROR);
        		resp = new AuthResponse(Constants.ADMIN_AUTH_ERROR, resp.getErrorCode());
        	}
        	else
        	{
        		ADMIN_ACCESS_TOKEN = resp.getAccessToken();
        	}
    	}
    }
    
    /**
     * 
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    public AuthResponse registerUser(AuthRequest request) throws JsonProcessingException
    {
    	
    	this.signInToMasterRealm(request);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.AUTHORIZATION, Constants.BEARER_TOKEN_PREFIX+ADMIN_ACCESS_TOKEN);//Bearer prefix is needed here by keycloak
        
        KeycloakRegistrationRequest kcRequest = prepareRequest(request);
        HttpEntity<KeycloakRegistrationRequest> requestEntity = new HttpEntity<>(kcRequest, headers);
        
        logger.info("Keycloak request entity: "+om.writerWithDefaultPrettyPrinter().writeValueAsString(requestEntity));
        
        ResponseEntity<String> responseEntity = this.callKeycloak(KEYCLOAK_USERS_URL, requestEntity, HttpMethod.POST);
        HttpStatusCode result = responseEntity.getStatusCode();
        
        this.logResponse(responseEntity, result);
        
        if(result.is2xxSuccessful())
        {
        	return new AuthResponse(Constants.SUCCESS_MESSAGE, "");
        }
        else
        {
        	return new AuthResponse(Constants.USER_REG_ERROR+request.getUserName(), Constants.ERROR_PREFIX+result);
        }
    }
    
    /**
     * 
     * @param request
     * @param refreshToken
     * @return
     */
    public AuthResponse refreshToken(AuthRequest request, String refreshToken)
    {
    	
        logger.debug("Keycloak token url: "+KEYCLOAK_TOKEN_URL);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString());
        String requestBody = "grant_type="+Constants.REFRESH_TOKEN_GRANT+"&refresh_token="+ refreshToken +
                             "&client_id="+ request.getClientId() +"&client_secret="+ request.getClientSecret();
        
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<String> responseEntity = this.callKeycloak(KEYCLOAK_TOKEN_URL, requestEntity, HttpMethod.POST);
        HttpStatusCode result = responseEntity.getStatusCode();
        this.logResponse(responseEntity, result);
        
        if(result.is2xxSuccessful())
        {
        	return retrieveTokens(responseEntity, false);
        }
        else
        {
        	logger.error(Constants.TOKEN_REGEN_ERROR, Constants.ERROR_PREFIX+result);
        	return new AuthResponse(Constants.TOKEN_REGEN_ERROR, Constants.ERROR_PREFIX+result);
        }
    }
    
    public AuthResponse retrieveByTaxId(AuthRequest request, String accessToken) throws JsonMappingException, JsonProcessingException
    {
    	this.signInToMasterRealm(request);
    	
        logger.debug("Keycloak user api url: "+KEYCLOAK_USERS_URL);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, Constants.BEARER_TOKEN_PREFIX+ADMIN_ACCESS_TOKEN);//Bearer prefix is needed here by keycloak
        
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = this.callKeycloak(KEYCLOAK_USERS_URL, requestEntity, HttpMethod.GET);
        HttpStatusCode result = responseEntity.getStatusCode();
        
        this.logResponse(responseEntity, result);
        
        if(result.is2xxSuccessful())
        {
        	return retrieveUserData(responseEntity, true, request.getTaxID());
        }
        else
        {
        	logger.error(Constants.TOKEN_REGEN_ERROR, Constants.ERROR_PREFIX+result);
        	return new AuthResponse(Constants.TOKEN_REGEN_ERROR, Constants.ERROR_PREFIX+result);
        }
    }
    
    /**
     * Retrieves the access/refresh tokens from the given response entity
     * @param responseEntity
     * @param isAdmin
     * @return
     */
    private AuthResponse retrieveTokens(ResponseEntity<String> responseEntity, boolean isAdmin)
    {
        String accessToken = "";
        String refreshToken = "";
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;
        
        try
		{
			jsonNode = mapper.readTree(responseEntity.getBody());
			
			if(!isAdmin)//Admin account does not have refresh tokens
			{
				refreshToken =  jsonNode.findValue(Constants.REFRESH_TOKEN_JSON_PROPERTY).textValue();
			}

			accessToken = jsonNode.findValue(Constants.ACCESS_TOKEN_JSON_PROPERTY).textValue();

	        logger.debug("Access token: "+accessToken);
	        logger.debug("Refresh token: "+refreshToken);
			
			return new AuthResponse(Constants.SUCCESS_MESSAGE, accessToken, refreshToken);
		}
        catch (JsonProcessingException e)
		{
			e.printStackTrace();
        	logger.error(Constants.KEYCLOAK_JSON_ERROR);
        	return new AuthResponse(Constants.KEYCLOAK_JSON_ERROR, Constants.KEYCLOAK_JSON_ERROR_CODE);
		}
    }
    
    private AuthResponse retrieveUserData(ResponseEntity<String> responseEntity, boolean isAdmin, String taxId)
    {
        try
		{
		    List<User> users = om.readValue(responseEntity.getBody(), new TypeReference<List<User>>() {});
		    
	        String email = getEmailByTaxId(users, taxId);

	        if (email != null) 
	        {
	            logger.debug("User email: " + email);
	        } else 
	        {
	        	logger.warn("No user found with the given tax_id.");
	        }
			
	        AuthResponse response = new AuthResponse(Constants.SUCCESS_MESSAGE, "", "");
	        response.setEmailAddress(email);
			return response;
		}
        catch (JsonProcessingException e)
		{
			e.printStackTrace();
        	logger.error(Constants.KEYCLOAK_JSON_ERROR);
        	return new AuthResponse(Constants.KEYCLOAK_JSON_ERROR, Constants.KEYCLOAK_JSON_ERROR_CODE);
		}
    }
    
    private String getEmailByTaxId(List<User> users, String taxId) 
    {
        for (User user : users) 
        {
            if (user.getAttributes() != null && user.getAttributes().containsKey(Constants.TAX_ID_JSON_PROPERTY)) 
            {
                List<String> taxIds = user.getAttributes().get(Constants.TAX_ID_JSON_PROPERTY);
                if(taxIds.size()>0)
                {
                	String userTaxId = taxIds.get(0);
                	if(taxId.equalsIgnoreCase(userTaxId) && user.getEmail()!=null)
                	{
                		return user.getEmail();
                	}
                }
                
            }
        }
        return null;
    }
    
    public AuthResponse checkTokenValidity(AuthRequest request, String accessToken)
    {
    	{
    		accessToken = accessToken.substring(Constants.BEARER_TOKEN_PREFIX.length());
    	}
    	
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString());
        String requestBody = "token="+ accessToken +"&client_id="+ request.getClientId() +"&client_secret="+ request.getClientSecret();
        
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<String> responseEntity = this.callKeycloak(KEYCLOAK_TOKEN_CHECK_URL, requestEntity, HttpMethod.POST);
        HttpStatusCode result = responseEntity.getStatusCode();
        this.logResponse(responseEntity, result);
        
        if(result.is2xxSuccessful())
        {
        	//Extract access token from response
            boolean isActive;
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode;
            try
    		{
    			jsonNode = mapper.readTree(responseEntity.getBody());
    			isActive = jsonNode.findValue(Constants.ACTIVE_TOKEN_JSON_PROPERTY).asBoolean();
    			
    			if(isActive)
    			{
    				return new AuthResponse(Constants.TOKEN_IS_ACTIVE);
    			}
    			else
    			{
    				return new AuthResponse(Constants.TOKEN_IS_INACTIVE);
    			}
    		}
            catch (JsonProcessingException e)
    		{
    			e.printStackTrace();
            	logger.error(Constants.KEYCLOAK_JSON_ERROR);
            	return new AuthResponse(Constants.KEYCLOAK_JSON_ERROR, Constants.KEYCLOAK_JSON_ERROR_CODE);
    		}
        }
        else
        {
        	logger.error(Constants.TOKEN_VALIDITY_ERROR, Constants.ERROR_PREFIX+result);
        	return new AuthResponse(Constants.TOKEN_VALIDITY_ERROR, Constants.ERROR_PREFIX+result);
        }
    }
    
    /**
     * 
     * @param request
     * @return
     */
    private KeycloakRegistrationRequest prepareRequest(AuthRequest request)
    {
    	
    	KeycloakRegistrationRequest kcRequest = new KeycloakRegistrationRequest(request.getUserName(), request.getFirstName(), request.getLastName(), request.getEmail(), false, true, null, request.getTaxID());
    	
    	KeycloakRegistrationRequest.Credential credential = kcRequest.new Credential(false, Constants.PASSWORD_GRANT, request.getPassword());
    	
    	List<Credential> credentials = new ArrayList<>();
    	credentials.add(credential);
    	
    	kcRequest.setCredentials(credentials);
    	
    	return kcRequest;
    }
	
    /**
     * 
     * @param url
     * @param requestEntity
     * @param httpMethod
     * @return
     */
    private ResponseEntity<String> callKeycloak(String url, HttpEntity<?> requestEntity, HttpMethod httpMethod)
    {
    	RestTemplate restTemplate = new RestTemplate();
    	try
    	{
    		ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, requestEntity, String.class);
    		return responseEntity;
    	}
    	catch(HttpClientErrorException ex)
    	{
    		ResponseEntity<String> responseEntity = new ResponseEntity<String>("", ex.getStatusCode());
    		logger.error("Caught HttpClientErrorException: "+ex.getMessage());
    		return responseEntity;
    	}
    }
    
    /**
     * 
     * @param responseEntity
     * @param result
     */
    private void logResponse(ResponseEntity<String> responseEntity, HttpStatusCode result)
    {
        logger.info("Response result code: "+result+"\nResponse entity body: "+responseEntity.getBody()+"\nResponse entity headers size: "+responseEntity.getHeaders().size());
    }
    
}

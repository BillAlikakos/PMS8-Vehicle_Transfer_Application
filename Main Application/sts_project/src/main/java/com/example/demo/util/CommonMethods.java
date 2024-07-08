package com.example.demo.util;

import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.datatypes.NotificationRequest;
import com.example.demo.datatypes.NotificationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CommonMethods
{
	/**
	 * Calls the notification service in order to send the necessary notification to the given user.
	 * @param request - NotificationRequest  object containing the parameters for the email notification.
	 * @param url - The service endpoint.
	 * @return
	 */
	@Async
	public CompletableFuture<NotificationResponse> callNotificationService(NotificationRequest request, String url)
	{
		RestTemplate client = new RestTemplate();
		HttpEntity<NotificationRequest> requestEntity = new HttpEntity<>(request);
		HttpEntity<NotificationResponse> response = client.exchange(url,HttpMethod.POST, requestEntity , NotificationResponse.class);
		
		return CompletableFuture.completedFuture(response.getBody());
	}
	
	/**
	 * Retrieves the given attribute from a user's bearer token.
	 * @param accessToken - The user's token.
	 * @param attributeToCheck = The attribute to be retrieved.
	 * @return <b>Optional&lt;String&gt;</b> object that contains the requested field, provided that it exists in the token.
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public Optional<String> retrieveTokenData(String accessToken, String attributeToCheck) throws JsonMappingException, JsonProcessingException
	{
		if(accessToken.startsWith(Constants.BEARER_TOKEN_PREFIX))
		{
			accessToken = accessToken.substring(Constants.BEARER_TOKEN_PREFIX.length());
		}
		
        String[] tokenParts = accessToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String jwt_header = new String(decoder.decode(tokenParts[0]));
        String jwt_payload = new String(decoder.decode(tokenParts[1]));
        
        ObjectMapper om = new ObjectMapper();
        JsonNode jsonNode = om.readTree(jwt_payload);
        
        String retVal;
        if(attributeToCheck.equalsIgnoreCase(Constants.JWT_EXPIRATION_ATTR))
        {
        	retVal = String.valueOf(checkTokenValidity(jsonNode));
        }
        else
        {
        	retVal = jsonNode.findValue(attributeToCheck).asText();
        }
        
        return Optional.of(retVal);
	}
	
	/**
	 * Checks if the given token is valid.
	 * @param tokenData - The JsonNode object containing the value.
	 * @return Whether the given token is still valid.
	 */
	private boolean checkTokenValidity(JsonNode tokenData)
	{
		long validTo = tokenData.findValue(Constants.JWT_EXPIRATION_ATTR).asLong();
		
		return System.currentTimeMillis() >= validTo ? false : true;
	}
}

package com.bill.userAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bill.userAuth.dataTypes.AuthRequest;
import com.bill.userAuth.dataTypes.AuthResponse;
import com.bill.userAuth.service.KeycloakAuthService;
import com.bill.userAuth.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/userAuth")
public class WebController
{

	@Autowired
	private KeycloakAuthService auth;
	
	@PostMapping(value="/login")
	@ResponseBody
	public ResponseEntity<AuthResponse> logIn(@Valid @RequestBody AuthRequest request) throws JsonProcessingException
	{
		AuthResponse response = auth.authenticateUser(request.getUserName(), request.getPassword(), request.getClientId(), request.getClientSecret(), false);
		return handleResponse(response);
	}
	
	@PostMapping(value="/register")
	@ResponseBody
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request) throws JsonProcessingException
	{
		AuthResponse response = auth.registerUser(request);
		return handleResponse(response);
	}
	
	@PostMapping(value="/checkToken")
	@ResponseBody
	public ResponseEntity<AuthResponse> checkToken(@Valid @RequestBody AuthRequest request,  @RequestHeader("authorization") String accessToken) throws JsonProcessingException
	{
		AuthResponse response = auth.checkTokenValidity(request, accessToken);
		return handleResponse(response);
	}
	
	@PostMapping(value="/refresh")
	@ResponseBody
	public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody AuthRequest request, @RequestHeader("refresh_token") String refreshToken) throws JsonProcessingException
	{
		AuthResponse response = auth.refreshToken(request, refreshToken);
		return handleResponse(response);
	}
	
	@PostMapping(value="/getData")
	@ResponseBody
	public ResponseEntity<AuthResponse> getUsersEmail(@Valid @RequestBody AuthRequest request, @RequestHeader("authorization") String accessToken) throws JsonProcessingException
	{
		AuthResponse response = auth.retrieveByTaxId(request, accessToken);
		return handleResponse(response);
	}

	/**
	 * @param response - The result of the incoming request's processing.
	 * @return the <b>ResponseEntity</b> object of the given response.
	 * @param response
	 * @return
	 */
	private ResponseEntity<AuthResponse> handleResponse(AuthResponse response)
	{
		if(response.getMessage()!=null && response.getMessage().equals(Constants.KEYCLOAK_JSON_ERROR))
		{
			return new ResponseEntity<AuthResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else if(response.getErrorCode()!=null && response.getErrorCode().contains(Constants.ERROR_PREFIX))
		{
			return new ResponseEntity<AuthResponse>(response, HttpStatus.resolve(response.getHttpStatusCode()));
		}
		else
		{
			try
			{
				return new ResponseEntity<>(response, HttpStatus.resolve(response.getHttpStatusCode()));
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return new ResponseEntity<AuthResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
	}
	
}

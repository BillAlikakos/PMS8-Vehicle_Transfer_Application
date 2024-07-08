package com.bill.userAuth.util;

import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonMethods
{

	public static boolean checkTokenValidity(String accessToken) throws JsonMappingException, JsonProcessingException
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
        
		long validTo = jsonNode.findValue(Constants.JWT_EXPIRATION_ATTR).asLong();
		
		return System.currentTimeMillis() >= validTo ? false : true;
	}
}

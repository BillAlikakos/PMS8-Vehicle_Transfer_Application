package com.bill.userAuth.dataTypes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bill.userAuth.util.Constants;

public class AuthResponse
{
	private String message;
	private String errorCode;
	private String accessToken;
	private String refreshToken;
	private String emailAddress;
	
	public AuthResponse(String message, String errorCode)
	{
		this.message = message;
		this.errorCode = errorCode;
	}

	public AuthResponse(String message, String accessToken, String refreshToken)
	{
		this.message = message;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
	
	public AuthResponse(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public String getErrorCode()
	{
		return errorCode;
	}
	
	public int getHttpStatusCode()
	{
		if(this.errorCode!=null)
		{
			Pattern pattern = Pattern.compile(Constants.ERROR_PREFIX+"\\d{3}", Pattern.CASE_INSENSITIVE);
	        
		    Matcher matcher = pattern.matcher(this.errorCode);
		    boolean matchFound = matcher.find();
		    if(matchFound) 
		    {
		    	return Integer.valueOf(this.errorCode.split(" ")[1]);
		    }
		    else
		    {
		    	return 200;
		    }
		}
		else
		{
			return 200;
		}
	}
	
	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public String getAccessToken()
	{
		return accessToken;
	}

	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}

	public String getRefreshToken()
	{
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken)
	{
		this.refreshToken = refreshToken;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}
	
}

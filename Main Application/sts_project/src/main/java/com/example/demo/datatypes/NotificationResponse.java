package com.example.demo.datatypes;

public class NotificationResponse
{
	private String result;
	private String errorCode;
	private String message;
	
	public NotificationResponse(String result, String errorCode, String message)
	{
		this.result = result;
		this.errorCode = errorCode;
		this.message = message;
	}
	
	public String getResult()
	{
		return result;
	}
	public void setResult(String result)
	{
		this.result = result;
	}
	public String getErrorCode()
	{
		return errorCode;
	}
	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
}

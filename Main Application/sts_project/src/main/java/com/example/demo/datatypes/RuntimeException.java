package com.example.demo.datatypes;

public class RuntimeException extends Exception
{
	private String errorCode;
	private String errorMessage;
	
	public RuntimeException(String errorCode, String errorMessage)
	{
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public String getErrorCode()
	{
		return errorCode;
	}
	
	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}
	
	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}
}

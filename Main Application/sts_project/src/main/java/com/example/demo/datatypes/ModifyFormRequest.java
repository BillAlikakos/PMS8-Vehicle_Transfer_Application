package com.example.demo.datatypes;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ModifyFormRequest
{
	@NotNull
	@NotEmpty
	private String status;
	
	@NotNull
	@NotEmpty
	private String userID;
	
	@NotNull
	@NotEmpty
	private String formID;
	
	private String email;
	
	public ModifyFormRequest() {}
	
	public ModifyFormRequest(String userID, String formID, String status)
	{
		this.status = status;	
		this.userID = userID;
		this.status = status;
	}
	
	public ModifyFormRequest(String userID, String formID, String status, String email)
	{
		this.status = status;	
		this.userID = userID;
		this.status = status;
		this.email = email;
	}
	
	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public String getFormID()
	{
		return formID;
	}

	public void setFormID(String formID)
	{
		this.formID = formID;
	}
	
	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
}

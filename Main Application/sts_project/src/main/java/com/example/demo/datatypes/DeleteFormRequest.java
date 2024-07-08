package com.example.demo.datatypes;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class DeleteFormRequest
{
	@NotNull
	@NotEmpty
	private String formID;
	
	@NotNull
	@NotEmpty
	private String userID;

	public DeleteFormRequest() {}
	
	public DeleteFormRequest(String userID, String formID)
	{
		this.userID = userID;
		this.formID = formID;
	}

	public String getFormID()
	{
		return formID;
	}

	public void setFormID(String FormID)
	{
		this.formID = FormID;
	}
	
	public String getUserID()
	{
		return this.userID;
	}
	
	public void setUserID(String userID)
	{
		this.userID = userID;
	}
}

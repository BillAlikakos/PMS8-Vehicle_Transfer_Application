package com.example.demo.datatypes;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.db.Form;

public class FormsResponse extends Response
{
	private List<Form> forms;
	
	public FormsResponse() {};
	
	public FormsResponse (String result, String errorCode, String message, ArrayList<Form> forms)
	{
		this.result = result;
		this.errorCode = errorCode;
		this.message = message;
		this.forms = forms;
	}
	
	public List<Form> getForms()
	{
		return forms;
	}
	public void setForms(List<Form> forms)
	{
		this.forms = forms;
	}

}

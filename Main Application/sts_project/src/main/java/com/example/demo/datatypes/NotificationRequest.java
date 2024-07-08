package com.example.demo.datatypes;

public class NotificationRequest
{
	private String recipient;
	private String formID;
	private boolean isOwner;
	private boolean isApproved;
	private boolean isComplete;
	private FormData data;
	
	public NotificationRequest(String recipient, String formID, boolean isOwner, boolean isApproved, boolean isComplete, FormData data)
	{
		this.recipient = recipient;
		this.formID = formID;
		this.isOwner = isOwner;
		this.isApproved = isApproved;
		this.isComplete = isComplete;
		this.data = data;
	}

	public String getRecipient()
	{
		return recipient;
	}

	public void setRecipient(String recipient)
	{
		this.recipient = recipient;
	}

	public String getFormID()
	{
		return formID;
	}

	public void setFormID(String formID)
	{
		this.formID = formID;
	}

	public boolean getIsOwner()
	{
		return isOwner;
	}

	public void setIsOwner(boolean isOwner)
	{
		this.isOwner = isOwner;
	}
	
	public boolean getIsApproved()
	{
		return isApproved;
	}

	public void setIsApproved(boolean isApproved)
	{
		this.isApproved = isApproved;
	}
	
	public boolean getIsComplete()
	{
		return isComplete;
	}
	
	public void setIsComplete(boolean isComplete)
	{
		this.isComplete = isComplete;
	}
	
	public void setData(FormData data)
	{
		this.data = data;
	}
	
	public FormData getData()
	{
		return data;
	}
	
}

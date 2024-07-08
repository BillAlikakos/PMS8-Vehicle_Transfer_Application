package com.bill.userAuth.dataTypes;

public class AuthRequest
{
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String clientId;
	private String clientSecret;
	private String taxID;
	
	public AuthRequest() {}
	
	public AuthRequest(String userName, String password, String clientId, String clientSecret)
	{
		this.userName = userName;
		this.password = password;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}
	
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getClientId()
	{
		return clientId;
	}

	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}

	public String getClientSecret()
	{
		return clientSecret;
	}

	public void setClientSecret(String clientSecret)
	{
		this.clientSecret = clientSecret;
	}

	public String getTaxID()
	{
		return taxID;
	}

	public void setTaxID(String taxID)
	{
		this.taxID = taxID;
	}

}

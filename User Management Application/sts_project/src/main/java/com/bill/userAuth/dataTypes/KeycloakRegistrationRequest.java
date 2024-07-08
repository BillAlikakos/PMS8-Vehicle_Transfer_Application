package com.bill.userAuth.dataTypes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KeycloakRegistrationRequest
{
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private boolean emailVerified;
	private boolean enabled;
	private List<Credential> credentials;
	@JsonProperty("attributes")
	private Attributes attributes;
	
	public KeycloakRegistrationRequest(String username, String firstName, String lastName, String email,
			boolean emailVerified, boolean enabled, List<Credential> credentials, String tax_id)
	{
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.emailVerified = emailVerified;
		this.enabled = enabled;
		this.credentials = credentials;
		this.attributes = new Attributes();
		this.attributes.setTax_id(tax_id);	
	}
	
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
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

	public boolean getEmailVerified()
	{
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified)
	{
		this.emailVerified = emailVerified;
	}

	public boolean getEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public List<Credential> getCredentials()
	{
		return credentials;
	}

	public void setCredentials(List<Credential> credentials)
	{
		this.credentials = credentials;
	}

	public class Credential
	{
		private boolean temporary;
		private String type;
		private String value;
		
		public Credential(boolean temporary, String type, String value)
		{
			this.temporary = temporary;
			this.type = type;
			this.value = value;
		}

		public boolean getTemporary()
		{
			return temporary;
		}

		public void setTemporary(boolean temporary)
		{
			this.temporary = temporary;
		}

		public String getType()
		{
			return type;
		}

		public void setType(String type)
		{
			this.type = type;
		}

		public String getValue()
		{
			return value;
		}

		public void setValue(String value)
		{
			this.value = value;
		}
	}

	private class Attributes
	{
		@JsonProperty("tax_id")
		private String tax_id;

		public String getTax_id()
		{
			return tax_id;
		}

		public void setTax_id(String tax_id)
		{
			this.tax_id = tax_id;
		}
	}
}
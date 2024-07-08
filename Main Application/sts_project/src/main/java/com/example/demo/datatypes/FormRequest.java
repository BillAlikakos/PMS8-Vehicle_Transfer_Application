package com.example.demo.datatypes;

import java.util.Date;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Validated
public class FormRequest
{
	private String formID;

	@NotNull(message="OwnerID is required")
	@NotEmpty(message="OwnerID is required")
	private String ownerID;
	
	@NotNull(message="VehicleID is required")
	@NotEmpty(message="VehicleID is required")
	private String vehicleID;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Athens")
	private Date date;
	private String status;
	private String isUpdate;
	
	@NotNull(message="taxID is required")
	@NotEmpty(message="taxID is required")
	private String taxID;
	
	private String email;
	
	public FormRequest() 
	{
	}
	
	public FormRequest(String ownerID, String buyerID, String vehicleID, Date date, String status, String isUpdate, String taxID)
	{
		super();
		if(date==null)
		{
			this.date = new Date(System.currentTimeMillis());
		}
		
		this.formID = java.util.UUID.randomUUID().toString();
		this.ownerID = ownerID;
		this.vehicleID = vehicleID;
		this.date = date;
		this.status = status;
		this.isUpdate = isUpdate;
		this.taxID = taxID;
	}
	
	public FormRequest(String ownerID, String buyerID, String vehicleID, Date date, String status, String isUpdate, String taxID, String email)
	{
		super();
		
		if(date==null)
		{
			this.date = new Date(System.currentTimeMillis());
		}
		
		this.formID = java.util.UUID.randomUUID().toString();
		this.ownerID = ownerID;
		this.vehicleID = vehicleID;
		this.date = date;
		this.status = status;
		this.isUpdate = isUpdate;
		this.taxID = taxID;
		this.email = email;
	}

	public String getOwnerID()
	{
		return ownerID;
	}
	
	public void setOwnerID(String ownerID)
	{
		this.ownerID = ownerID;
	}
	
	public String getVehicleID()
	{
		return vehicleID;
	}

	public Date getDate()
	{
		return date;
	}

	public String getStatus()
	{
		return this.status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getFormID()
	{
		return formID;
	}

	public void setFormID(String formID)
	{
		this.formID = formID;
	}

	public boolean getIsUpdate()
	{
		return Boolean.valueOf(isUpdate);
	}

	public void setIsUpdate(String isUpdate)
	{
		this.isUpdate = isUpdate;
	}
	
	public String getTaxID()
	{
		return this.taxID;
	}
	
	public void setTaxID(String taxID)
	{
		this.taxID = taxID;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
}
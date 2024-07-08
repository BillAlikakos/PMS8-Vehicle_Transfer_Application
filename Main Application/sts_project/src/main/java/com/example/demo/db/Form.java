package com.example.demo.db;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="forms", uniqueConstraints= @UniqueConstraint(columnNames = {"form_id"})) 
public class Form 
{
	@Id
	@Column(unique=true)
	private String form_ID;
	@Column(name="owner_ID")
	private final String owner_ID;
	private final String vehicle_ID;
	private final Date date_submitted;
	private String status;
	private final String tax_ID; 
	
	public Form() 
	{
		this.owner_ID = "";
		this.vehicle_ID = "";
		this.date_submitted = new Date(System.currentTimeMillis());
		this.tax_ID = "";
	}
	
	public Form(String FormId,String ownerID, String vehicleID, Date date, String status, String taxID)
	{
		this.form_ID = FormId;
		this.owner_ID = ownerID;
		this.vehicle_ID = vehicleID;
		
		if(date!=null)
		{
			this.date_submitted = date;	
		}
		else
		{
			this.date_submitted = new Date(System.currentTimeMillis());
		}
		
		this.status = status;
		this.tax_ID = taxID;
	}


	public String getOwner_ID()
	{
		return owner_ID;
	}
	
	public String getVehicle_ID()
	{
		return vehicle_ID;
	}

	public Date getDate()
	{
		return date_submitted;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getForm_ID()
	{
		return form_ID;
	}

	public void setForm_ID(String form_ID)
	{
		this.form_ID = form_ID;
	}
	
	public String getTax_ID()
	{
		return this.tax_ID;
	}
	
	@Override
	public String toString()
	{
		return "FormId: "+this.form_ID+" OwnerId: "+this.owner_ID+" TaxId: "+this.tax_ID+" VehicleId: "+this.vehicle_ID+" dateSubmitted: "+this.date_submitted+" status: "+this.status;
	}
}

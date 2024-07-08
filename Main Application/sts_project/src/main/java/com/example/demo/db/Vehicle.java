package com.example.demo.db;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="vehicles", uniqueConstraints= @UniqueConstraint(columnNames = {"vehicle_id"})) 
public class Vehicle
{
	@Id
	@Column(unique=true, name="vehicle_id")
	private final String vehicleId;
	private final String make;
	private final String model;
	private final int year;
	private String color;
	private final int displacement;
	@Column(name="status")
	private String status;
	@Column(name="owner_id")
	private String ownerId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy",  timezone = "Europe/Athens", lenient = OptBoolean.FALSE)
	private Date dateOfInspection;
	
	public Vehicle()
	{
		this.vehicleId = "";
		this.make = "";
		this.model = "";
		this.year = 0;
		this.displacement = 0;
	}
	
	public Vehicle(String vehicleId, String make, String model, int year, String color, int displacement,
			String status, String ownerId, Date dateOfInspection)
	{
		this.vehicleId = vehicleId;
		this.make = make;
		this.model = model;
		this.year = year;
		this.color = color;
		this.displacement = displacement;
		this.status = status;
		this.ownerId = ownerId;
		this.dateOfInspection = dateOfInspection;
	}
	
	public String getVehicleId()
	{
		return vehicleId;
	}
	
	public String getMake()
	{
		return make;
	}
	
	public String getModel()
	{
		return model;
	}

	public int getYear()
	{
		return year;
	}

	public String getColor()
	{
		return color;
	}
	
	public void setColor(String color)
	{
		this.color = color;
	}
	
	public int getDisplacement()
	{
		return displacement;
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
		return ownerId;
	}
	
	public void setUserID(String userID)
	{
		this.ownerId = userID;
	}
	
	public String getOwnerId()
	{
		return ownerId;
	}
	
	public void setOwnerId(String ownerId)
	{
		this.ownerId = ownerId;
	}
	
	public Date getDateOfInspection()
	{
		return dateOfInspection;
	}
	
	public void setDateOfInspection(Date dateOfInspection)
	{
		this.dateOfInspection = dateOfInspection;
	}
}

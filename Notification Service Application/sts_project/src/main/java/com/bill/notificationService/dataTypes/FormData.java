package com.bill.notificationService.dataTypes;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;

public class FormData
{
	private String vehicleID;
	private String firstName;
	private String lastName;
	private String make;
	private int year;
	private int displacement;
	private String model;
	private String color;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy",  timezone = "Europe/Athens", lenient = OptBoolean.FALSE)
	private Date dateOfInspection;
	
	public FormData()
	{
	}
	
	public FormData(String vehicleID, String firstName, String lastName, String make, int year, int displacement,
			String model, String color, Date dateOfInspection)
	{
		this.vehicleID = vehicleID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.make = make;
		this.year = year;
		this.displacement = displacement;
		this.model = model;
		this.color = color;
		this.dateOfInspection = dateOfInspection;
	}
	
	public String getVehicleID()
	{
		return vehicleID;
	}
	
	public void setVehicleID(String vehicleID)
	{
		this.vehicleID = vehicleID;
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
	
	public String getMake()
	{
		return make;
	}
	
	public void setMake(String make)
	{
		this.make = make;
	}
	
	public int getYear()
	{
		return year;
	}
	
	public void setYear(int year)
	{
		this.year = year;
	}
	
	public int getDisplacement()
	{
		return displacement;
	}
	
	public void setDisplacement(int displacement)
	{
		this.displacement = displacement;
	}
	
	public String getModel()
	{
		return model;
	}
	
	public void setModel(String model)
	{
		this.model = model;
	}
	
	public String getColor()
	{
		return color;
	}
	
	public void setColor(String color)
	{
		this.color = color;
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

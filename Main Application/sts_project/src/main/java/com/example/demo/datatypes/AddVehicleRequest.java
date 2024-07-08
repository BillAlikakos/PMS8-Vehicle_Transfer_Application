package com.example.demo.datatypes;

import java.util.Date;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Validated
public class AddVehicleRequest
{
	@NotNull(message="vehicleId is required")
	@NotEmpty(message="vehicleId is required")
	private String vehicleId;
	
	@NotNull(message="make is required")
	@NotEmpty(message="make is required")
	private String make;
	
	@NotNull(message="model is required")
	@NotEmpty(message="model is required")
	private String model;
	
	@NotNull(message="color is required")
	@NotEmpty(message="color is required")
	private String color;
	
	@NotNull(message="year is required")
	private int year;
	
	@NotNull(message="displacement is required")
	private int displacement;
	
	@NotNull(message="userId is required")
	@NotEmpty(message="userId is required")
	private String ownerId;
	
	@NotNull(message="dateOfInspection is required")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy",  timezone = "Europe/Athens", lenient = OptBoolean.FALSE)
	private Date dateOfInspection;
	
	public AddVehicleRequest() {}

	public AddVehicleRequest(
			@NotNull(message = "vehicleId is required") @NotEmpty(message = "vehicleId is required") String vehicleId,
			@NotNull(message = "make is required") @NotEmpty(message = "make is required") String make,
			@NotNull(message = "model is required") @NotEmpty(message = "model is required") String model,
			@NotNull(message = "color is required") @NotEmpty(message = "color is required") String color,
			@NotNull(message = "year is required") @NotEmpty(message = "year is required") int year,
			@NotNull(message = "displacement is required") @NotEmpty(message = "displacement is required") int displacement,
			@NotNull(message = "userId is required") @NotEmpty(message = "userId is required") String userId,
			@NotNull(message = "dateOfInspection is required") @NotEmpty(message = "dateOfInspection is required") Date  dateOfInspection)
	{
		this.vehicleId = vehicleId;
		this.make = make;
		this.model = model;
		this.color = color;
		this.year = year;
		this.displacement = displacement;
		this.ownerId = userId;
		this.dateOfInspection = dateOfInspection;
	}

	public String getVehicleId()
	{
		return vehicleId;
	}

	public void setVehicleId(String vehicleId)
	{
		this.vehicleId = vehicleId;
	}

	public String getMake()
	{
		return make;
	}

	public void setMake(String make)
	{
		this.make = make;
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

	public String getOwnerId()
	{
		return ownerId;
	}

	public void setOwnerId(String ownerId)
	{
		this.ownerId = ownerId;
	}

	public Date  getDateOfInspection()
	{
		return dateOfInspection;
	}

	public void setDateOfInspection(Date  dateOfInspection)
	{
		this.dateOfInspection = dateOfInspection;
	}
}

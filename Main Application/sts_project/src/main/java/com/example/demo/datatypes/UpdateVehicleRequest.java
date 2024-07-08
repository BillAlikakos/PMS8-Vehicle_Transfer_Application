package com.example.demo.datatypes;

import java.util.Date;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Validated
public class UpdateVehicleRequest
{
	@NotNull(message="vehicleId is required")
	@NotEmpty(message="vehicleId is required")
	private String vehicleId;
	
	private String color;
	
	private String ownerId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy",  timezone = "Europe/Athens")
	private Date dateOfInspection;
	
	private String status;
	
	public UpdateVehicleRequest() {}

	public UpdateVehicleRequest(@NotNull(message = "vehicleId is required") @NotEmpty(message = "vehicleId is required") String vehicleId, String color,
			String userId, Date dateOfInspection,String status)
	{
		this.vehicleId = vehicleId;
		this.color = color;
		this.ownerId = userId;
		this.dateOfInspection = dateOfInspection;
		this.status = status;
	}

	public String getVehicleId()
	{
		return vehicleId;
	}

	public void setVehicleId(String vehicleId)
	{
		this.vehicleId = vehicleId;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
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
	
	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
	
}

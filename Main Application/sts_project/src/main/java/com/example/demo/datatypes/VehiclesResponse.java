package com.example.demo.datatypes;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.db.Vehicle;

public class VehiclesResponse extends Response
{
	private List<Vehicle> vehicles;
	
	public VehiclesResponse() {};
	
	public VehiclesResponse (String result, String errorCode, String message, ArrayList<Vehicle> vehicles)
	{
		this.result = result;
		this.errorCode = errorCode;
		this.message = message;
		this.vehicles = vehicles;
	}
	
	public List<Vehicle> getVehicles()
	{
		return vehicles;
	}
	public void setVehicles(List<Vehicle> vehicles)
	{
		this.vehicles = vehicles;
	}

	@Override
	public String toString()
	{
		String retVal = "";
		for(int i=0;i<this.vehicles.size();i++)
		{
			Vehicle current = this.vehicles.get(i);
			if(current!=null)
			{
				retVal+="VehicleId: "+current.getVehicleId()+" Make: "+current.getMake()+" Color: "+current.getColor()+" Displacement: "+current.getDisplacement()
				+" OwnerId: "+current.getOwnerId()+" Status: "+current.getStatus()+" UserID: "+current.getUserID()+" Year: "+current.getYear()+" Date of Inspection: "+current.getDateOfInspection()+"\n";				
			}
		}
		
		return retVal;
	}
}

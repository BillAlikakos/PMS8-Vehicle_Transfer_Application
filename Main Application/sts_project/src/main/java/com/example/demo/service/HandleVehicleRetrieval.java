package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.datatypes.VehiclesResponse;
import com.example.demo.db.Vehicle;
import com.example.demo.db.VehicleRepository;

@Service
public class HandleVehicleRetrieval
{

	@Autowired
	private VehicleRepository vehicleRepo;
	
	/**
	 * Retrieves all the vehicles of a given user.
	 * @param ownerId - The id of the user that will be used to retrieve the vehicles from the db.
	 * @return a list of all the owned vehicles that the given user owns.
	 */
	public VehiclesResponse retrieveVehicles(String ownerId)
	{	
		ArrayList<Vehicle> userVehicles = vehicleRepo.findByOwnerId(ownerId);
		
		return new VehiclesResponse("", null, null, userVehicles);
	}
	
	/**
	 * Retrieves the data of the given vehicle.
	 * @param vehicleId - The id of the vehicle to retrieve.
	 * @return the data of the requested vehicle.
	 */
	public VehiclesResponse retrieveVehicleById(String vehicleId)
	{	
		Vehicle vehicle = vehicleRepo.findVehicleByVehicleId(vehicleId);
		
		ArrayList<Vehicle> retVal = new ArrayList<>();
		retVal.add(vehicle);
		
		return new VehiclesResponse("", null, null, retVal);
	}
	
	
}

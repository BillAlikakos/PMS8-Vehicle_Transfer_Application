package com.example.demo.service;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.datatypes.AddVehicleRequest;
import com.example.demo.datatypes.Response;
import com.example.demo.db.Vehicle;
import com.example.demo.db.VehicleRepository;
import com.example.demo.util.Constants;

@Service
public class HandleVehicleAddition
{
	@Autowired 
	private VehicleRepository vehicleRepo;
	
	/**
	 * Handles the addition of a new vehicle for a given user.
	 * @param request - The data of the vehicle to add.
	 * @return - The result of the operation.
	 */
	public Response addVehicle(AddVehicleRequest request)
	{
		Optional<Vehicle> existingVehicleOptional = vehicleRepo.findById(request.getVehicleId());
		
		if(existingVehicleOptional.isPresent())
		{
			return new Response(Constants.FAILURE_STATUS_CODE, Constants.DB_ERR,Constants.ENTITY_ALREADY_EXISTS);
		}

		Vehicle vehicleDTO = new Vehicle(request.getVehicleId(), request.getMake(), request.getModel(), request.getYear(), request.getColor(), request.getDisplacement(), Constants.VEHICLE_STATUS.AVAILABLE.toString(), request.getOwnerId(), request.getDateOfInspection());
		
		vehicleRepo.save(vehicleDTO);
		
		String resultMsg = MessageFormat.format(Constants.SUCCESSFULLY_INSERTED, Constants.ENTITY_VEHICLE, vehicleDTO.getVehicleId());
	
		return new Response(Constants.SUCCESS_STATUS_CODE,"", resultMsg);
	}

}

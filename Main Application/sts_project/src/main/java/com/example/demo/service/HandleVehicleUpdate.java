package com.example.demo.service;

import java.text.MessageFormat;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.datatypes.Response;
import com.example.demo.datatypes.UpdateVehicleRequest;
import com.example.demo.db.Vehicle;
import com.example.demo.db.VehicleRepository;
import com.example.demo.util.Constants;

@Service
public class HandleVehicleUpdate
{
	@Autowired 
	private VehicleRepository vehicleRepo;
	private Logger logger;
	
	/**
	 * Handles the update of a vehicle's data provided that it exists.
	 * @param request - The updated data of the vehicle.
	 * @return - The result of the operation.
	 */
	public Response updateVehicle(UpdateVehicleRequest request)
	{
		logger = LogManager.getLogger();
		Optional<Vehicle> vehicle = vehicleRepo.findById(request.getVehicleId());
		
		if(vehicle.isPresent())
		{
			logger.info("[updateVehicle] Updating vehicle "+request.getVehicleId());
			
			Vehicle current = vehicle.get();
			
			if(request.getColor()!=null)
			{
				current.setColor(request.getColor());
			}
			
			if(request.getDateOfInspection()!=null)
			{
				current.setDateOfInspection(request.getDateOfInspection());
			}
			
			if(request.getOwnerId()!=null)
			{
				current.setOwnerId(request.getOwnerId());
			}
			
			if(request.getStatus()!=null)
			{
				current.setStatus(request.getStatus());
			}
			
			vehicleRepo.save(current);
			
			String resultMsg = MessageFormat.format(Constants.SUCCESSFULLY_UPDATED, Constants.ENTITY_VEHICLE, request.getVehicleId());
			
			return new Response(Constants.SUCCESS_STATUS_CODE,"", resultMsg);
		}
		else
		{
			String resultMsg = MessageFormat.format(Constants.VEHICLE_DOES_NOT_EXIST, request.getVehicleId());
			
			return new Response(Constants.FAILURE_STATUS_CODE,Constants.DB_ERR, resultMsg);
		}
		
		
	}
}

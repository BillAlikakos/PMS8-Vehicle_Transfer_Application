package com.example.demo.db;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle,String>
{
	ArrayList<Vehicle> findByOwnerId(String ownerId);
	Vehicle findVehicleByVehicleId(String vehicleId);
}

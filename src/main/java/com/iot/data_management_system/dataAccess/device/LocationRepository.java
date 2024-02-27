package com.iot.data_management_system.dataAccess.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iot.data_management_system.entities.device.Location;

public interface LocationRepository extends JpaRepository<Location, Integer>{
	
	@Query(value = "SELECT EXISTS ("
			+ "  SELECT * FROM locations"
			+ "  WHERE longitude = :longitude AND latitude = :latitude"
			+ "); ", nativeQuery = true)
	boolean exists(double longitude, double latitude);
	
	@Query(value = "select * from locations l " +
			"where longitude = :longitude " + 
			"and latitude = :latitude ", nativeQuery = true)
	Location findByLocation(double longitude, double latitude);
}

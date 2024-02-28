package com.iot.data_management_system.dataAccess.device;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iot.data_management_system.entities.device.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {

	@Query(value = "SELECT * FROM sensors s WHERE s.name = :name", nativeQuery = true)
	List<Sensor> findByName(String name);
	Sensor findBySerialNumber(String num);
	boolean existsByName(String name);
	boolean existsBySerialNumber(String num);
}

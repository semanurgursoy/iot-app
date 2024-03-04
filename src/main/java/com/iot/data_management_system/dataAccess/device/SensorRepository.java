package com.iot.data_management_system.dataAccess.device;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iot.data_management_system.entities.device.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {

	Sensor findBySerialNumber(String num);

	boolean existsByName(String name);

	boolean existsBySerialNumber(String num);

	@Query(value = "SELECT * FROM sensors s WHERE s.name = :name", nativeQuery = true)
	List<Sensor> findByName(String name);

	@Query(value = "SELECT COUNT(s) = COUNT(DISTINCT s.serial_number) FROM sensors s WHERE s.serial_number IN :sensors", nativeQuery = true)
	boolean checkSensorsExists(List<String> sensors);
	
	@Query(value = "SELECT * FROM sensors "
			+ "WHERE serial_number IN (:sensors);", nativeQuery = true)
	List<Sensor> convertTypeToSensor(List<String> sensors);

}

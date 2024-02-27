package com.iot.data_management_system.dataAccess.device;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iot.data_management_system.entities.device.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {

	Sensor findByName(String name);
	Sensor findBySerialNumber(String num);
	boolean existsByName(String name);
	boolean existsBySerialNumber(String num);
}

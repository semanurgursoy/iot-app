package com.iot.data_management_system.Business.abstracts.device;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.iot.data_management_system.entities.dto.SensorDto;

public interface SensorService {

	ResponseEntity<List<SensorDto>> getAll();
	ResponseEntity<List<SensorDto>> getByName(String name);
	ResponseEntity<SensorDto> getBySerialNumber(String num);
	
	ResponseEntity<HttpStatus> add(SensorDto sensorDto);
    ResponseEntity<HttpStatus> update(SensorDto sensorDto);
    ResponseEntity<HttpStatus> delete(int id);
}

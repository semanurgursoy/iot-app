package com.iot.data_management_system.Business.abstracts;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.iot.data_management_system.entities.dto.DeviceDto;

public interface DeviceService {

	ResponseEntity<List<DeviceDto>> getAll();
	ResponseEntity<DeviceDto> getBySerialNumber(String num);
	
	ResponseEntity<HttpStatus> add(DeviceDto deviceDto);
    ResponseEntity<HttpStatus> update(DeviceDto deviceDto);
    ResponseEntity<HttpStatus> delete(int id);
}

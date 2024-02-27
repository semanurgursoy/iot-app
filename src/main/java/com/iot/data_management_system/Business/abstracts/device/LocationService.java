package com.iot.data_management_system.Business.abstracts.device;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.iot.data_management_system.entities.dto.LocationDto;

public interface LocationService {

	List<LocationDto> getAll();
	
	ResponseEntity<HttpStatus> add(LocationDto locationDto);
    ResponseEntity<HttpStatus> update(LocationDto locationDto);
    ResponseEntity<HttpStatus> delete(int id);
}

package com.iot.data_management_system.Business.abstracts.device;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.iot.data_management_system.entities.device.Mode;
import com.iot.data_management_system.entities.dto.ModeDto;

public interface ModeService {
	
	List<ModeDto> getAll();
	ModeDto getByName(String mode);
	
	ResponseEntity<HttpStatus> add(Mode mode);
    ResponseEntity<HttpStatus> update(String mode, String newMode);
    ResponseEntity<HttpStatus> delete(int id);

}
package com.iot.data_management_system.Business.abstracts.device;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.iot.data_management_system.entities.dto.ModeDto;

public interface ModeService {
	
	ResponseEntity<List<ModeDto>> getAll();
	ResponseEntity<ModeDto> getByName(String mode);
	
	ResponseEntity<HttpStatus> add(ModeDto modeDto);
    ResponseEntity<HttpStatus> update(String mode, String newMode);
    ResponseEntity<HttpStatus> delete(int id);

}
package com.iot.data_management_system.Business.abstracts.device;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.iot.data_management_system.entities.device.Brand;
import com.iot.data_management_system.entities.dto.BrandDto;

public interface BrandService {

	List<BrandDto> getAll();
	BrandDto getByName(String brand);
	
	ResponseEntity<HttpStatus> add(Brand brand);
    ResponseEntity<HttpStatus> update(String brand, String newBrand);
    ResponseEntity<HttpStatus> delete(int id);
    
}

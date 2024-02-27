package com.iot.data_management_system.WebAPIs.device;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.data_management_system.Business.abstracts.device.LocationService;
import com.iot.data_management_system.entities.dto.LocationDto;

@RestController
@RequestMapping("/api/location")
public class LocationController {

	private LocationService locationService;

	public LocationController(LocationService locationService) {
		this.locationService = locationService;
	}
	
	@GetMapping("/getall")
	public List<LocationDto> getAll(){
		return locationService.getAll();
	}
	
	@PostMapping("/add")
	public ResponseEntity<HttpStatus> add(@RequestBody LocationDto locationDto){
		return locationService.add(locationDto);
	}
	
	@PostMapping("/update")
	public ResponseEntity<HttpStatus> update(@RequestBody LocationDto locationDto){
		return locationService.update(locationDto);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<HttpStatus> delete(int id){
		return locationService.delete(id);
	}
}

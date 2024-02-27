package com.iot.data_management_system.WebAPIs.device;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.data_management_system.Business.abstracts.device.SensorService;
import com.iot.data_management_system.entities.dto.SensorDto;

@RestController
@RequestMapping("/api/sensor")
public class SensorController {
	
	private SensorService sensorService;

	public SensorController(SensorService sensorService) {
		this.sensorService = sensorService;
	}
	
	@GetMapping("/getall")
	public List<SensorDto> getAll(){
		return sensorService.getAll();
	}
	
	@GetMapping("/get_by_name")
	public SensorDto getByName(String name){
		return sensorService.getByName(name);
	}
	
	@GetMapping("/get_by_serial_number")
	public SensorDto getBySerialNumber(String serialNumber){
		return sensorService.getBySerialNumber(serialNumber);
	}
	
	@PostMapping("/add")
	public ResponseEntity<HttpStatus> add(@RequestBody SensorDto dto){
		return sensorService.add(dto);
	}
	
	@PostMapping("/update")
	public ResponseEntity<HttpStatus> update(@RequestBody SensorDto dto){
		return sensorService.update(dto);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<HttpStatus> delete(int id){
		return sensorService.delete(id);
	}
}

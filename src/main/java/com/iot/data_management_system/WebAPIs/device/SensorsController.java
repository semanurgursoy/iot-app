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
@RequestMapping("/api/sensors")
public class SensorsController {
	
	private SensorService sensorService;

	public SensorsController(SensorService sensorService) {
		this.sensorService = sensorService;
	}
	
	@GetMapping("/getall")
	public ResponseEntity<List<SensorDto>> getAll(){
		return sensorService.getAll();
	}
	
	@GetMapping("/getall_by_name")
	public ResponseEntity<List<SensorDto>> getByName(String name){
		return sensorService.getByName(name);
	}
	
	@GetMapping("/get_by_serial_number")
	public ResponseEntity<SensorDto> getBySerialNumber(String serialNumber){
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

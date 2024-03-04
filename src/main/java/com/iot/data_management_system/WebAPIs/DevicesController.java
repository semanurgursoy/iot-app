package com.iot.data_management_system.WebAPIs;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.data_management_system.Business.abstracts.DeviceService;
import com.iot.data_management_system.entities.dto.DeviceDto;

@RestController
@RequestMapping("/api/devices")
public class DevicesController {
	
	private DeviceService deviceService;

	public DevicesController(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
	
	@GetMapping("/getall")
	public ResponseEntity<List<DeviceDto>> getAll(){
		return deviceService.getAll();
	}
	
	@GetMapping("/get_by_serial_number")
	public ResponseEntity<DeviceDto> getBySerialNumber(String serialNumber){
		return deviceService.getBySerialNumber(serialNumber);
	}
	
	@PostMapping("/add")
	public ResponseEntity<HttpStatus> add(@RequestBody DeviceDto dto){
		return deviceService.add(dto);
	}
	
	@PostMapping("/update")
	public ResponseEntity<HttpStatus> update(@RequestBody DeviceDto dto){
		return deviceService.update(dto);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<HttpStatus> delete(int id){
		return deviceService.delete(id);
	}

}

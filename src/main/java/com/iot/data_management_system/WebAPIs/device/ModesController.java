package com.iot.data_management_system.WebAPIs.device;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.data_management_system.Business.abstracts.device.ModeService;
import com.iot.data_management_system.entities.dto.ModeDto;



@RestController
@RequestMapping("/api/modes")
public class ModesController {
	
	private ModeService modeService;

	public ModesController(ModeService modeService) {
		this.modeService = modeService;
	}
	
	@GetMapping("/getall")
	public ResponseEntity<List<ModeDto>> getAll(){
		return modeService.getAll();
	}
	
	@GetMapping("/get_by_mode")
	public ResponseEntity<ModeDto> getByMode(String mode){
		return modeService.getByName(mode);
	}
	
	@PostMapping("/add")
	public ResponseEntity<HttpStatus> add(@RequestBody ModeDto modeDto){
		return modeService.add(modeDto);
	}
	
	@PostMapping("/update")
	public ResponseEntity<HttpStatus> update(String mode, String newMode){
		return modeService.update(mode, newMode);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<HttpStatus> delete(int id){
		return modeService.delete(id);
	}
}

package com.iot.data_management_system.WebAPIs;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.data_management_system.Business.abstracts.SignalService;
import com.iot.data_management_system.entities.dto.SignalDto;

@RestController
@RequestMapping("/api/ingestion")
public class IngestionController {

	private SignalService signalService;
	
	public IngestionController(SignalService signalService) {
		this.signalService = signalService;
	}

	@PostMapping("/add_measurement")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody SignalDto dto) {
		return signalService.add(dto);
    }
	
	@PostMapping("/add_measurements")
    public ResponseEntity<HttpStatus> addMeasurements(@RequestBody List<SignalDto> dtoList) {
		return signalService.add(dtoList);
    }
}

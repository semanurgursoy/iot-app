package com.iot.data_management_system.WebAPIs;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.data_management_system.Business.abstracts.SignalService;
import com.iot.data_management_system.entities.Signal;

@RestController
@RequestMapping("/api/ingestion")
public class IngestionController {

	private SignalService signalService;
	
	public IngestionController(SignalService signalService) {
		this.signalService = signalService;
	}

	@PostMapping("/addMeasurement")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody Signal signal) {
		return signalService.add(signal);
    }
	
	@PostMapping("/addMeasurements")
    public ResponseEntity<HttpStatus> addMeasurements(@RequestBody List<Signal> signals) {
		return signalService.add(signals);
    }
}

package com.iot.data_management_system.WebAPIs;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.data_management_system.entities.Signal;

@RestController
@RequestMapping("/api/ingestion")
public class IngestionController {

	
	@PostMapping("/addMeasurement")
    public <T> ResponseEntity<T> addMeasurement(Signal signal) {
		return null;
    }
	
	@PostMapping("/addMeasurements")
    public <T> ResponseEntity<T> addMeasurements(List<Signal> signals) {
		return null;
    }
}

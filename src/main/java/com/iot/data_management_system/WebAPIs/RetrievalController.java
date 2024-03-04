package com.iot.data_management_system.WebAPIs;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.data_management_system.Business.abstracts.SignalService;
import com.iot.data_management_system.entities.dto.SignalDto;

@RestController
@RequestMapping("/api/retrieval")
public class RetrievalController {

	private SignalService signalService;
	
	public RetrievalController(SignalService signalService) {
		this.signalService = signalService;
	}
	
	@GetMapping("/getall")
	public ResponseEntity<List<SignalDto>> getAll(){
		return signalService.getAll();
	}
	
	@GetMapping("/getall_by_device")
	public ResponseEntity<List<SignalDto>> getAllByDevice(String serialNumber){
		return signalService.getAllByDevice(serialNumber);
	}
	
	@GetMapping("/getall_by_date")
	public ResponseEntity<List<SignalDto>> getAllByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
		return signalService.getAllByDate(date);
	}
	
	@GetMapping("/getall_by_temperature")
	public ResponseEntity<List<SignalDto>> getAllByTemperature(double temperature, int condition){
		return signalService.getAllByTemperature(temperature, condition);
	}
	
	@GetMapping("/getall_by_humidity")
	public ResponseEntity<List<SignalDto>> getAllByHumidity(double humidity, int condition){
		return signalService.getAllByHumidity(humidity, condition);
	}
	
	@GetMapping("/getall_by_wind_speed")
	public ResponseEntity<List<SignalDto>> getAllByWindSpeed(double windSpeed, int condition){
		return signalService.getAllByWindSpeed(windSpeed, condition);
	}
	
	@GetMapping("/getall_by_light_intensity")
	public ResponseEntity<List<SignalDto>> getAllByLightIntensity(double lightIntensity, int condition){
		return signalService.getAllByLightIntensity(lightIntensity, condition);
	}
	
	@GetMapping("/get_by_id")
	public ResponseEntity<SignalDto> getMeasurementById(int id){
		return signalService.getById(id);
	}
}

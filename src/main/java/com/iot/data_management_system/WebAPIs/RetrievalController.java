package com.iot.data_management_system.WebAPIs;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.data_management_system.Business.abstracts.SignalService;
import com.iot.data_management_system.entities.Signal;

@RestController
@RequestMapping("/api/retrieval")
public class RetrievalController {

	private SignalService signalService;
	
	public RetrievalController(SignalService signalService) {
		this.signalService = signalService;
	}
	
	@GetMapping("/getall")
	public List<Signal> getAll(){
		return signalService.getAll();
	}
	
	@GetMapping("/getall_by_deviceid")
	public List<Signal> getAllByDeviceId(int id){
		return signalService.getAllByDeviceId(id);
	}
	
	@GetMapping("/getall_by_date")
	public List<Signal> getAllByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
		return signalService.getAllByDate(date);
	}
	
	@GetMapping("/getall_by_temperature")
	public List<Signal> getAllByTemperature(double temperature, int condition){
		return signalService.getAllByTemperature(temperature, condition);
	}
	
	@GetMapping("/getall_by_humidity")
	public List<Signal> getAllByHumidity(double humidity, int condition){
		return signalService.getAllByHumidity(humidity, condition);
	}
	
	@GetMapping("/getall_by_windspeed")
	public List<Signal> getAllByWindSpeed(double windSpeed, int condition){
		return signalService.getAllByWindSpeed(windSpeed, condition);
	}
	
	@GetMapping("/getall_by_lightintensity")
	public List<Signal> getAllByLightIntensity(double lightIntensity, int condition){
		return signalService.getAllByLightIntensity(lightIntensity, condition);
	}
	
	@GetMapping("/get_measurement_by_id")
	public Optional<Signal> getMeasurementById(int id){
		return signalService.getById(id);
	}
}

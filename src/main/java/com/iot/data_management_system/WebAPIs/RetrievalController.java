package com.iot.data_management_system.WebAPIs;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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
	public List<SignalDto> getAll(){
		return signalService.getAll();
	}
	
	@GetMapping("/getall_by_device")
	public List<SignalDto> getAllByDevice(String serialNumber){
		return signalService.getAllByDevice(serialNumber);
	}
	
	@GetMapping("/getall_by_date")
	public List<SignalDto> getAllByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
		return signalService.getAllByDate(date);
	}
	
	@GetMapping("/getall_by_temperature")
	public List<SignalDto> getAllByTemperature(double temperature, int condition){
		return signalService.getAllByTemperature(temperature, condition);
	}
	
	@GetMapping("/getall_by_humidity")
	public List<SignalDto> getAllByHumidity(double humidity, int condition){
		return signalService.getAllByHumidity(humidity, condition);
	}
	
	@GetMapping("/getall_by_wind_speed")
	public List<SignalDto> getAllByWindSpeed(double windSpeed, int condition){
		return signalService.getAllByWindSpeed(windSpeed, condition);
	}
	
	@GetMapping("/getall_by_light_intensity")
	public List<SignalDto> getAllByLightIntensity(double lightIntensity, int condition){
		return signalService.getAllByLightIntensity(lightIntensity, condition);
	}
	
	@GetMapping("/get_by_id")
	public SignalDto getMeasurementById(int id){
		return signalService.getById(id);
	}
}

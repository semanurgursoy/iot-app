package com.iot.data_management_system.Business.abstracts;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.iot.data_management_system.entities.Signal;
import com.iot.data_management_system.entities.dto.SignalDto;

public interface SignalService {
	ResponseEntity<List<SignalDto>> getAll();
	ResponseEntity<List<SignalDto>> getAllByDevice(String serialNumber);
	ResponseEntity<List<SignalDto>> getAllByDate(LocalDate date);
	ResponseEntity<List<SignalDto>> getAllByTemperature(double temperature, int condition);
	ResponseEntity<List<SignalDto>> getAllByHumidity(double humidity, int condition);
	ResponseEntity<List<SignalDto>> getAllByWindSpeed(double windSpeed, int condition);
	ResponseEntity<List<SignalDto>> getAllByLightIntensity(double lightIntensity, int condition);
    ResponseEntity<SignalDto> getById(int id);

    ResponseEntity<HttpStatus> add(SignalDto dto);
    ResponseEntity<HttpStatus> add(List<SignalDto> dtoList);
    ResponseEntity<HttpStatus> update(Signal signal);
    ResponseEntity<HttpStatus> delete(int id);
}

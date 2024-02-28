package com.iot.data_management_system.Business.abstracts;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.iot.data_management_system.entities.Signal;
import com.iot.data_management_system.entities.dto.SignalDto;

public interface SignalService {
	List<SignalDto> getAll();
	List<SignalDto> getAllByDevice(String serialNumber);
	List<SignalDto> getAllByDate(LocalDate date);
	List<SignalDto> getAllByTemperature(double temperature, int condition);
	List<SignalDto> getAllByHumidity(double humidity, int condition);
	List<SignalDto> getAllByWindSpeed(double windSpeed, int condition);
	List<SignalDto> getAllByLightIntensity(double lightIntensity, int condition);
    SignalDto getById(int id);

    ResponseEntity<HttpStatus> add(SignalDto dto);
    ResponseEntity<HttpStatus> add(List<SignalDto> dtoList);
    ResponseEntity<HttpStatus> update(Signal signal);
    ResponseEntity<HttpStatus> delete(int id);
}

package com.iot.data_management_system.Business.abstracts;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.iot.data_management_system.entities.Signal;
import com.iot.data_management_system.entities.dto.SignalDto;

public interface SignalService {
	List<SignalDto> getAll();
	List<SignalDto> getAllByDeviceId(int id);
	List<SignalDto> getAllByDate(LocalDate date);
	List<SignalDto> getAllByTemperature(double temperature, int condition);
	List<SignalDto> getAllByHumidity(double humidity, int condition);
	List<SignalDto> getAllByWindSpeed(double windSpeed, int condition);
	List<SignalDto> getAllByLightIntensity(double lightIntensity, int condition);
    SignalDto getById(int id);

    ResponseEntity<HttpStatus> add(Signal signal);
    ResponseEntity<HttpStatus> add(List<Signal> signals);
    ResponseEntity<HttpStatus> update(Signal signal);
    ResponseEntity<HttpStatus> delete(int id);
}

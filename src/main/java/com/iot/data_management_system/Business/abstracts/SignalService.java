package com.iot.data_management_system.Business.abstracts;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.iot.data_management_system.entities.Signal;

public interface SignalService {
	List<Signal> getAll();
	List<Signal> getAllByDeviceId(int id);
	List<Signal> getAllByDate(LocalDate date);
	List<Signal> getAllByTemperature(double temperature, int condition);
	List<Signal> getAllByHumidity(double humidity, int condition);
	List<Signal> getAllByWindSpeed(double windSpeed, int condition);
	List<Signal> getAllByLightIntensity(double lightIntensity, int condition);
    Optional<Signal> getById(int id);

    ResponseEntity<HttpStatus> add(Signal signal);
    ResponseEntity<HttpStatus> add(List<Signal> signals);
    ResponseEntity<HttpStatus> update(Signal signal);
    ResponseEntity<HttpStatus> delete(int id);
}

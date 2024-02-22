package com.iot.data_management_system.Business.abstracts;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.iot.data_management_system.entities.Signal;

public interface SignalService {
	List<Signal> getAll();
	List<Signal> getAllByDeviceId(int id);
    Optional<Signal> getById(int id);

    ResponseEntity<HttpStatus> add(Signal signal);
    ResponseEntity<HttpStatus> add(List<Signal> signals);
    ResponseEntity<HttpStatus> update(Signal signal);
    ResponseEntity<HttpStatus> delete(int id);
}

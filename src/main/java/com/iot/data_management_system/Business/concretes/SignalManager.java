package com.iot.data_management_system.Business.concretes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iot.data_management_system.Business.abstracts.SignalService;
import com.iot.data_management_system.dataAccess.SignalRepository;
import com.iot.data_management_system.entities.Signal;

@Service
public class SignalManager implements SignalService {

	private SignalRepository signalRepository;
	
	public SignalManager(SignalRepository signalRepository) {
		this.signalRepository = signalRepository;
	}

	@Override
	public List<Signal> getAll() {
		return signalRepository.findAll();
	}

	@Override
	public List<Signal> getAllByDeviceId(int id) {
		return signalRepository.findByDeviceId(id);
	}

	@Override
	public Optional<Signal> getById(int id) {
		return signalRepository.findById(id);
	}

	@Override
	public ResponseEntity<HttpStatus> add(Signal signal) {
		signal.setLocalDateTime(LocalDateTime.now());
		signalRepository.save(signal);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<HttpStatus> add(List<Signal> signals) {
		for (Signal s : signals) {
            s.setLocalDateTime(LocalDateTime.now());
        }
		signalRepository.saveAll(signals);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<HttpStatus> update(Signal signal) {
		if(signalRepository.existsById(signal.getId())) {
			Signal s = signalRepository.getReferenceById(signal.getId());
			s.setDeviceId(signal.getDeviceId());
			s.setHumidity(signal.getHumidity());
			s.setLight_intensity(signal.getLight_intensity());
			s.setLocalDateTime(signal.getLocalDateTime());
			s.setTemperature(signal.getTemperature());
			s.setWind_speed(signal.getWind_speed());
			
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<HttpStatus> delete(int id) {
		if(signalRepository.existsById(id)) {
			signalRepository.deleteById(id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

}

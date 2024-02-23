package com.iot.data_management_system.Business.concretes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
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
	public List<Signal> getAllByDate(LocalDate date){
		return signalRepository.findByDate(date);
	}
	
	@Override
	public List<Signal> getAllByTemperature(double temperature, int condition){
		return signalRepository.findByTemperature(temperature, condition);
	}
	
	@Override
	public List<Signal> getAllByHumidity(double humidity, int condition){
		return signalRepository.findByHumidity(humidity, condition);
	}
	
	@Override
	public List<Signal> getAllByWindSpeed(double windSpeed, int condition){
		return signalRepository.findByWindSpeed(windSpeed, condition);
	}
	
	@Override
	public List<Signal> getAllByLightIntensity(double lightIntensity, int condition){
		return signalRepository.findByLightIntensity(lightIntensity, condition);
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
			s.setLightIntensity(signal.getLightIntensity());
			s.setLocalDateTime(signal.getLocalDateTime());
			s.setTemperature(signal.getTemperature());
			s.setWindSpeed(signal.getWindSpeed());
			
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

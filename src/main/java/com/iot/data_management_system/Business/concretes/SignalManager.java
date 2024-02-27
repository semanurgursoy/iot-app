package com.iot.data_management_system.Business.concretes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iot.data_management_system.Business.Utils;
import com.iot.data_management_system.Business.abstracts.SignalService;
import com.iot.data_management_system.dataAccess.DeviceRepository;
import com.iot.data_management_system.dataAccess.SignalRepository;
import com.iot.data_management_system.entities.Device;
import com.iot.data_management_system.entities.Signal;
import com.iot.data_management_system.entities.dto.SignalDto;

@Service
public class SignalManager implements SignalService {

	private SignalRepository signalRepository;
	private DeviceRepository deviceRepository;
	private ModelMapper modelMapper;
	// write custom model mapper
	
	public SignalManager(SignalRepository signalRepository, DeviceRepository deviceRepository, ModelMapper modelMapper) {
		this.signalRepository = signalRepository;
		this.deviceRepository = deviceRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<SignalDto> getAll() {
		List<Signal> signalList = signalRepository.findAll();
		List<SignalDto> dtoList = new ArrayList<SignalDto>();
		for(Signal s: signalList)
			dtoList.add(modelMapper.map(s, SignalDto.class));
		return dtoList;
	}

	@Override
	public List<SignalDto> getAllByDeviceId(int id) {
		List<Signal> signalList = signalRepository.findByDeviceId(id);
		List<SignalDto> dtoList = new ArrayList<SignalDto>();
		for(Signal s: signalList)
			dtoList.add(modelMapper.map(s, SignalDto.class));
		return dtoList;
	}
	
	@Override
	public List<SignalDto> getAllByDate(LocalDate date){
		List<Signal> signalList = signalRepository.findByDate(date);
		List<SignalDto> dtoList = new ArrayList<SignalDto>();
		for(Signal s: signalList)
			dtoList.add(modelMapper.map(s, SignalDto.class));
		return dtoList;
	}
	
	@Override
	public List<SignalDto> getAllByTemperature(double temperature, int condition){
		List<Signal> signalList = signalRepository.findByTemperature(temperature, condition);
		List<SignalDto> dtoList = new ArrayList<SignalDto>();
		for(Signal s: signalList)
			dtoList.add(modelMapper.map(s, SignalDto.class));
		return dtoList;
	}
	
	@Override
	public List<SignalDto> getAllByHumidity(double humidity, int condition){
		List<Signal> signalList = signalRepository.findByHumidity(humidity, condition);
		List<SignalDto> dtoList = new ArrayList<SignalDto>();
		for(Signal s: signalList)
			dtoList.add(modelMapper.map(s, SignalDto.class));
		return dtoList;
	}
	
	@Override
	public List<SignalDto> getAllByWindSpeed(double windSpeed, int condition){
		List<Signal> signalList = signalRepository.findByWindSpeed(windSpeed, condition);
		List<SignalDto> dtoList = new ArrayList<SignalDto>();
		for(Signal s: signalList)
			dtoList.add(modelMapper.map(s, SignalDto.class));
		return dtoList;
	}
	
	@Override
	public List<SignalDto> getAllByLightIntensity(double lightIntensity, int condition){
		List<Signal> signalList = signalRepository.findByLightIntensity(lightIntensity, condition);
		List<SignalDto> dtoList = new ArrayList<SignalDto>();
		for(Signal s: signalList)
			dtoList.add(modelMapper.map(s, SignalDto.class));
		return dtoList;
	}
	
	@Override
	public SignalDto getById(int id) {
		Signal signal = signalRepository.findById(id).get();
		SignalDto dto = modelMapper.map(signal, SignalDto.class);
		return dto;
	}

	@Override
	public ResponseEntity<HttpStatus> add(Signal signal) {
		signal.setLocalDateTime(LocalDateTime.now());
		Device device = deviceRepository.findBySerialNumber(signal.getDevice().getSerialNumber());
		signal.setDevice(device);
		signalRepository.save(signal);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<HttpStatus> add(List<Signal> signals) {
		for (Signal s : signals) {
            s.setLocalDateTime(LocalDateTime.now());
            Device device = deviceRepository.findBySerialNumber(s.getDevice().getSerialNumber());
    		s.setDevice(device);
        }
		signalRepository.saveAll(signals);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<HttpStatus> update(Signal signal) {
		if(signalRepository.existsById(signal.getId())) {
			Signal s = signalRepository.findById(signal.getId()).get();
			if(signal.getDevice().getSerialNumber() != null && deviceRepository.existsBySerialNumber(signal.getDevice().getSerialNumber()))
				s.setDevice(signal.getDevice());
			BeanUtils.copyProperties(signal, s, Utils.getNullPropertyNames(signal));
			
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

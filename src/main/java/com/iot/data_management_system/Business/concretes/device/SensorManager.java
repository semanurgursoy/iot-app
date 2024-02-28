package com.iot.data_management_system.Business.concretes.device;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iot.data_management_system.entities.dto.SensorDto;
import com.iot.data_management_system.Business.Utils;
import com.iot.data_management_system.Business.abstracts.device.SensorService;
import com.iot.data_management_system.dataAccess.device.BrandRepository;
import com.iot.data_management_system.dataAccess.device.ModeRepository;
import com.iot.data_management_system.dataAccess.device.SensorRepository;
import com.iot.data_management_system.entities.device.Sensor;

@Service
public class SensorManager implements SensorService {

	private SensorRepository sensorRepository;
	private ModeRepository modeRepository;
	private BrandRepository brandRepository;
	private ModelMapper modelMapper;
	

	public SensorManager(SensorRepository sensorRepository, ModeRepository modeRepository, 
			BrandRepository brandRepository, ModelMapper modelMapper) {
		this.sensorRepository = sensorRepository;
		this.modeRepository = modeRepository;
		this.brandRepository = brandRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<SensorDto> getAll() {
		List<Sensor> sensorList = sensorRepository.findAll();
		List<SensorDto> dtoList = new ArrayList<SensorDto>();
		for(Sensor sensor: sensorList)
			dtoList.add(modelMapper.map(sensor, SensorDto.class));
		return dtoList;
	}

	@Override
	public List<SensorDto> getByName(String name) {
		List<Sensor> sensorList = sensorRepository.findByName(name);
		List<SensorDto> dtoList = new ArrayList<SensorDto>();
		for(Sensor sensor: sensorList)
			dtoList.add(modelMapper.map(sensor, SensorDto.class));
		return dtoList;
	}

	@Override
	public SensorDto getBySerialNumber(String num) {
		Sensor sensor = sensorRepository.findBySerialNumber(num);
		SensorDto dto = modelMapper.map(sensor, SensorDto.class);
		return dto;
	}

	@Override
	public ResponseEntity<HttpStatus> add(SensorDto sensorDto) {
		if(!sensorRepository.existsBySerialNumber(sensorDto.getSerialNumber()) 
				&& modeRepository.existsByMode(sensorDto.getMode())
						&& brandRepository.existsByBrand(sensorDto.getBrand())) {
			Sensor sensor = new Sensor();
			sensor.setMode(modeRepository.findByMode(sensorDto.getMode()));
			sensor.setBrand(brandRepository.findByBrand(sensorDto.getBrand()));
			BeanUtils.copyProperties(sensorDto, sensor, Utils.getNullPropertyNames(sensorDto));
			
			sensorRepository.save(sensor);
			
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<HttpStatus> update(SensorDto sensorDto) {
		if (sensorRepository.existsBySerialNumber(sensorDto.getSerialNumber())) {
        	Sensor sensor = sensorRepository.findBySerialNumber(sensorDto.getSerialNumber());
        	if(sensorDto.getMode() != null) 
        		sensor.setMode(modeRepository.findByMode(sensorDto.getMode()));
        	if(sensorDto.getBrand() != null)
        		sensor.setBrand(brandRepository.findByBrand(sensorDto.getBrand()));
        	
            BeanUtils.copyProperties(sensorDto, sensor, Utils.getNullPropertyNames(sensorDto));

            sensorRepository.save(sensor);
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        }
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<HttpStatus> delete(int id) {
		if(sensorRepository.existsById(id)) {
			sensorRepository.deleteById(id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

}

package com.iot.data_management_system.Business.concretes.device;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iot.data_management_system.Business.Utils;
import com.iot.data_management_system.Business.abstracts.device.LocationService;
import com.iot.data_management_system.dataAccess.device.LocationRepository;
import com.iot.data_management_system.entities.device.Location;
import com.iot.data_management_system.entities.dto.LocationDto;

@Service
public class LocationManager implements LocationService {

	private LocationRepository locationRepository;
	private ModelMapper modelMapper;
	
	public LocationManager(LocationRepository locationRepository, ModelMapper modelMapper) {
		this.locationRepository = locationRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public ResponseEntity<List<LocationDto>> getAll() {
		List<Location> locationList = locationRepository.findAll();
		List<LocationDto> dtoList = new ArrayList<LocationDto>();
		for(Location location: locationList)
			dtoList.add(modelMapper.map(location, LocationDto.class));
		return new ResponseEntity<>(dtoList, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<HttpStatus> add(LocationDto locationDto) {
		if(!locationRepository.exists(locationDto.getLongitude(), locationDto.getLatitude())) {
			Location l = new Location();
			BeanUtils.copyProperties(locationDto, l, Utils.getNullPropertyNames(locationDto));
			
			locationRepository.save(l);
			
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<HttpStatus> update(LocationDto locationDto) {
		if (locationRepository.exists(locationDto.getLongitude(), locationDto.getLatitude())) {
        	Location l = locationRepository.findByLocation(locationDto.getLongitude(), locationDto.getLatitude());
            BeanUtils.copyProperties(locationDto, l, Utils.getNullPropertyNames(locationDto));

            locationRepository.save(l);
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        }
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<HttpStatus> delete(int id) {
		if(locationRepository.existsById(id)) {
			locationRepository.deleteById(id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

}

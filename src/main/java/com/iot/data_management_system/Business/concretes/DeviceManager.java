package com.iot.data_management_system.Business.concretes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iot.data_management_system.Business.Utils;
import com.iot.data_management_system.Business.abstracts.DeviceService;
import com.iot.data_management_system.dataAccess.DeviceRepository;
import com.iot.data_management_system.dataAccess.device.BrandRepository;
import com.iot.data_management_system.dataAccess.device.LocationRepository;
import com.iot.data_management_system.dataAccess.device.ModeRepository;
import com.iot.data_management_system.dataAccess.device.SensorRepository;
import com.iot.data_management_system.entities.Device;
import com.iot.data_management_system.entities.device.Sensor;
import com.iot.data_management_system.entities.dto.DeviceDto;

@Service
public class DeviceManager implements DeviceService {
	
	private DeviceRepository deviceRepository;
	private ModeRepository modeRepository;
	private BrandRepository brandRepository;
	private SensorRepository sensorRepository;
	private LocationRepository locationRepository;
	// write custom model mapper

	public DeviceManager(DeviceRepository deviceRepository, ModeRepository modeRepository, BrandRepository brandRepository,
			 SensorRepository sensorRepository, LocationRepository locationRepository) {
		this.deviceRepository = deviceRepository;
		this.modeRepository = modeRepository;
		this.brandRepository = brandRepository;
		this.sensorRepository = sensorRepository;
		this.locationRepository = locationRepository;
	}

	@Override
	public List<DeviceDto> getAll() {
		List<Device> deviceList = deviceRepository.findAll();
		List<DeviceDto> dtoList = new ArrayList<DeviceDto>();
		for(Device device: deviceList)
			dtoList.add(deviceMap(device));
		return dtoList;
	}

	@Override
	public DeviceDto getBySerialNumber(String num) {
		return deviceMap(deviceRepository.findBySerialNumber(num));
	}

	@Override
	public ResponseEntity<HttpStatus> add(DeviceDto deviceDto) {
		if(!deviceRepository.existsBySerialNumber(deviceDto.getSerialNumber()) 
			&& modeRepository.existsByMode(deviceDto.getMode())
				&& brandRepository.existsByBrand(deviceDto.getBrand())
					&& checkSensorsExists(deviceDto.getSensors())
						&& locationRepository.exists(deviceDto.getLongitude(), deviceDto.getLatitude())) {
			Device device = new Device();
			device.setMode(modeRepository.findByMode(deviceDto.getMode()));
			device.setBrand(brandRepository.findByBrand(deviceDto.getBrand()));
			device.setSensors(convertTypeToSensor(deviceDto.getSensors()));
			device.setLocation(locationRepository.findByLocation(deviceDto.getLongitude(), deviceDto.getLatitude()));
			BeanUtils.copyProperties(deviceDto, device, Utils.getNullPropertyNames(deviceDto));
			
			deviceRepository.save(device);
			
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<HttpStatus> update(DeviceDto deviceDto) {
		if (deviceRepository.existsBySerialNumber(deviceDto.getSerialNumber())) {
        	Device device = deviceRepository.findBySerialNumber(deviceDto.getSerialNumber());
        	if(deviceDto.getMode() != null && modeRepository.existsByMode(deviceDto.getMode())) 
        		device.setMode(modeRepository.findByMode(deviceDto.getMode()));
        	if(deviceDto.getBrand() != null && brandRepository.existsByBrand(deviceDto.getBrand()))
        		device.setBrand(brandRepository.findByBrand(deviceDto.getBrand()));
        	if(deviceDto.getSensors() != null && checkSensorsExists(deviceDto.getSensors()))
        		device.setSensors(convertTypeToSensor(deviceDto.getSensors()));
        	if(deviceDto.getLongitude() != 0 && deviceDto.getLatitude() != 0 && locationRepository.exists(deviceDto.getLongitude(), deviceDto.getLatitude()))
        		device.setLocation(locationRepository.findByLocation(deviceDto.getLongitude(), deviceDto.getLatitude()));
        	
            BeanUtils.copyProperties(deviceDto, device, Utils.getNullPropertyNames(deviceDto));

            deviceRepository.save(device);
            
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        }
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<HttpStatus> delete(int id) {
		if(deviceRepository.existsById(id)) {
			deviceRepository.deleteById(id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}
	
	private boolean checkSensorsExists(List<String> sensors) {
		for(String sensor: sensors) {
			if(!sensorRepository.existsBySerialNumber(sensor))
				return false;
		}
		return true;
	}
	
	private List<Sensor> convertTypeToSensor(List<String> sensors) {
		List<Sensor> list = new ArrayList<Sensor>();
		for(String sensor: sensors) {
			list.add(sensorRepository.findBySerialNumber(sensor));
		}
		return list;
	}
	
	private DeviceDto deviceMap(Device source) {
    	DeviceDto destination = new DeviceDto();
    	destination.setName(source.getName());
    	destination.setModel(source.getModel());
    	destination.setSerialNumber(source.getSerialNumber());
    	destination.setAim(source.getAim());
    	destination.setWeight(source.getWeight());
    	destination.setBattery(source.getBattery());
    	destination.setWarrantyPeriod(source.getWarrantyPeriod());
    	destination.setLastMaintenanceDate(source.getLastMaintenanceDate());
    	destination.setMode(source.getMode().getMode());
    	destination.setBrand(source.getBrand().getBrand());
    	destination.setLongitude(source.getLocation().getLongitude());
    	destination.setLatitude(source.getLocation().getLatitude());
    	destination.setFullLocation(source.getFullLocation());
        destination.setSensors(source.getSensorListWithNames());
        return destination;
	}
	

}

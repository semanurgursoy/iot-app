package com.iot.data_management_system.Business.concretes;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iot.data_management_system.Business.Utils;
import com.iot.data_management_system.Business.abstracts.DeviceService;
import com.iot.data_management_system.Business.config.mapper.DeviceToDeviceDtoConverter;
import com.iot.data_management_system.dataAccess.DeviceRepository;
import com.iot.data_management_system.dataAccess.device.BrandRepository;
import com.iot.data_management_system.dataAccess.device.LocationRepository;
import com.iot.data_management_system.dataAccess.device.ModeRepository;
import com.iot.data_management_system.dataAccess.device.SensorRepository;
import com.iot.data_management_system.entities.Device;
import com.iot.data_management_system.entities.dto.DeviceDto;

@Service
public class DeviceManager implements DeviceService {

	private DeviceRepository deviceRepository;
	private ModeRepository modeRepository;
	private BrandRepository brandRepository;
	private SensorRepository sensorRepository;
	private LocationRepository locationRepository;
	private ModelMapper modelMapper;

	public DeviceManager(DeviceRepository deviceRepository, ModeRepository modeRepository,
			BrandRepository brandRepository, SensorRepository sensorRepository, LocationRepository locationRepository,
			ModelMapper modelMapper) {
		this.deviceRepository = deviceRepository;
		this.modeRepository = modeRepository;
		this.brandRepository = brandRepository;
		this.sensorRepository = sensorRepository;
		this.locationRepository = locationRepository;
		this.modelMapper = modelMapper;
		modelMapper.addConverter(new DeviceToDeviceDtoConverter());
	}

	@Override
	public ResponseEntity<List<DeviceDto>> getAll() {
		List<Device> deviceList = deviceRepository.findAll();
		List<DeviceDto> dtoList = new ArrayList<DeviceDto>();
		for (Device device : deviceList)
			dtoList.add(modelMapper.map(device, DeviceDto.class));
		return new ResponseEntity<>(dtoList, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<DeviceDto> getBySerialNumber(String num) {
		if (deviceRepository.existsBySerialNumber(num))
			return new ResponseEntity<>(modelMapper.map(deviceRepository.findBySerialNumber(num), DeviceDto.class),
					HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<HttpStatus> add(DeviceDto deviceDto) {
		if (!deviceRepository.existsBySerialNumber(deviceDto.getSerialNumber())
				&& modeRepository.existsByMode(deviceDto.getMode())
				&& brandRepository.existsByBrand(deviceDto.getBrand())
				&& sensorRepository.checkSensorsExists(deviceDto.getSensors())
				&& locationRepository.exists(deviceDto.getLongitude(), deviceDto.getLatitude())) {
			Device device = new Device();
			device.setMode(modeRepository.findByMode(deviceDto.getMode()));
			device.setBrand(brandRepository.findByBrand(deviceDto.getBrand()));
			device.setSensors(sensorRepository.convertTypeToSensor(deviceDto.getSensors()));
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
			if (deviceDto.getMode() != null && modeRepository.existsByMode(deviceDto.getMode()))
				device.setMode(modeRepository.findByMode(deviceDto.getMode()));
			if (deviceDto.getBrand() != null && brandRepository.existsByBrand(deviceDto.getBrand()))
				device.setBrand(brandRepository.findByBrand(deviceDto.getBrand()));
			if (deviceDto.getSensors() != null && sensorRepository.checkSensorsExists(deviceDto.getSensors()))
				device.setSensors(sensorRepository.convertTypeToSensor(deviceDto.getSensors()));
			if (deviceDto.getLongitude() != 0 && deviceDto.getLatitude() != 0
					&& locationRepository.exists(deviceDto.getLongitude(), deviceDto.getLatitude()))
				device.setLocation(
						locationRepository.findByLocation(deviceDto.getLongitude(), deviceDto.getLatitude()));

			BeanUtils.copyProperties(deviceDto, device, Utils.getNullPropertyNames(deviceDto));

			deviceRepository.save(device);

			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<HttpStatus> delete(int id) {
		if (deviceRepository.existsById(id)) {
			deviceRepository.deleteById(id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

}

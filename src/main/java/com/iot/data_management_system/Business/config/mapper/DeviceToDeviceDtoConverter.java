package com.iot.data_management_system.Business.config.mapper;

import org.modelmapper.AbstractConverter;

import com.iot.data_management_system.entities.Device;
import com.iot.data_management_system.entities.dto.DeviceDto;

public class DeviceToDeviceDtoConverter extends AbstractConverter<Device, DeviceDto> {

	@Override
	protected DeviceDto convert(Device source) {
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
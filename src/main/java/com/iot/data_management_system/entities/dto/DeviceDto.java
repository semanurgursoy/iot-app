package com.iot.data_management_system.entities.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {
	
	private String name;
	private String model;
	private String serialNumber;
	private String aim;
	private String weight;
	private String battery;
	private String warrantyPeriod;
	private LocalDate lastMaintenanceDate;
	private String mode;
	private String brand;
	private double longitude;
	private double latitude;
	private String fullLocation;
	private List<String> sensors;
}

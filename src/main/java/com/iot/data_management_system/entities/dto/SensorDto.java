package com.iot.data_management_system.entities.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SensorDto {

	private String name;
	private String model;
	private String serialNumber;
	private String connectionType;
	private String aim;
	private String weight;
	private String battery;
	private String warrantyPeriod;
	private String measuringRange;
	private String reactionTime;
	private boolean calibration;
	private boolean waterResistant;
	private boolean dustResistant;
	private boolean heatResistant;
	private boolean impactResistant;
	private String mode;
	private String brand;

}

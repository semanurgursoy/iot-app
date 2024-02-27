package com.iot.data_management_system.entities.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignalDto {
	
	private String device;
	private LocalDateTime localDateTime; 
	private double temperature;
	private double humidity;
	private double windSpeed;
	private double lightIntensity;
}

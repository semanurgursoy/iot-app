package com.iot.data_management_system.entities.dto;

import java.time.LocalDateTime;

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
public class SignalDto {
	
	private String device;
	private LocalDateTime localDateTime; 
	private double temperature;
	private double humidity;
	private double windSpeed;
	private double lightIntensity;
}

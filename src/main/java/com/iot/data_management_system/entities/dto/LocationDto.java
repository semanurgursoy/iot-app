package com.iot.data_management_system.entities.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LocationDto {
	
	private String country;
	private String city;
	private String street;
	private double latitude;
	private double longitude;
	
}

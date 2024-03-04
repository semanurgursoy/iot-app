package com.iot.data_management_system.entities.dto;

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
public class LocationDto {
	
	private String country;
	private String city;
	private String street;
	private double longitude;
	private double latitude;
	
}

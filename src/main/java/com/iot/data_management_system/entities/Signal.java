package com.iot.data_management_system.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="signals")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Signal {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
	private int id;
	
	@Column(name="device_id")
	private int deviceId;
	
	@Column(name="timestamp")
	private LocalDateTime localDateTime; 
	
	@Column(name="temperature")
	private double temperature;
	
	@Column(name="humidity")
	private double humidity;
	
	@Column(name="wind_speed")
	private double wind_speed;
	
	@Column(name="light_intensity")
	private double light_intensity;

}

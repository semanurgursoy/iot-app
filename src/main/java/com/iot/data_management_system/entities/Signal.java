package com.iot.data_management_system.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
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
	
	@ManyToOne
    @JoinColumn(name = "device_id")
	private Device device;
	
	@Column(name="timestamp")
	private LocalDateTime localDateTime; 
	
	@Column(name="temperature")
	private double temperature;
	
	@Column(name="humidity")
	private double humidity;
	
	@Column(name="wind_speed")
	private double windSpeed;
	
	@Column(name="light_intensity")
	private double lightIntensity;

}

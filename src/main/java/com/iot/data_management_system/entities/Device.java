package com.iot.data_management_system.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iot.data_management_system.entities.device.Brand;
import com.iot.data_management_system.entities.device.Location;
import com.iot.data_management_system.entities.device.Mode;
import com.iot.data_management_system.entities.device.Sensor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="devices")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Device {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="model")
	private String model;
	
	@Column(name="serial_number")
	private String serialNumber;
	
	@Column(name="aim")
	private String aim;
	
	@Column(name="weight")
	private String weight;
	
	@Column(name="battery")
	private String battery;
	
	@Column(name="warranty_period")
	private String warrantyPeriod;
	
	@Column(name="last_maintenance_date")
	private LocalDate lastMaintenanceDate;
	
	@ManyToOne
    @JoinColumn(name = "brand_id")
	private Brand brand;
	
	@ManyToOne
    @JoinColumn(name = "mode_id")
	private Mode mode;
	
	@ManyToMany
    @JoinTable(
            name = "device_sensors",
            joinColumns = @JoinColumn(name="device_id"),
            inverseJoinColumns = @JoinColumn(name="sensor_id"))
    @JsonIgnoreProperties("devices")
	private List<Sensor> sensors;
	
	@ManyToOne
    @JoinColumn(name = "location_id")
	private Location location;
	
	public List<String> getSensorListWithNames(){
		List<String> list = new ArrayList<String>();
		for(Sensor sensor: this.sensors)
			list.add(sensor.getName());
		return list;
	}
	
	public String getFullLocation() {
		return this.location.getCountry() + " / " + this.location.getCity() + " / " + this.location.getStreet() +
				"/ " + this.location.getLongitude() + " - " + this.location.getLatitude();
	}
}

package com.iot.data_management_system.entities.device;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iot.data_management_system.entities.Device;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="sensors")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sensor {

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
	
	@Column(name="connection_type")
	private String connectionType;
	
	@Column(name="aim")
	private String aim;
	
	@Column(name="weight")
	private String weight;
	
	@Column(name="battery")
	private String battery;
	
	@Column(name="warranty_period")
	private String warrantyPeriod;
	
	@Column(name="measuring_range")
	private String measuringRange;
	
	@Column(name="reaction_time")
	private String reactionTime;
	
	@Column(name="calibration")
	private boolean calibration;
	
	@Column(name="water_resistant")
	private boolean waterResistant;
	
	@Column(name="dust_resistant")
	private boolean dustResistant;
	
	@Column(name="heat_resistant")
	private boolean heatResistant;
	
	@Column(name="impact_resistant")
	private boolean impactResistant;
	
	@ManyToOne
    @JoinColumn(name = "mode_id")
	private Mode mode;
	
	@ManyToOne
    @JoinColumn(name = "brand_id")
	private Brand brand;
	
	@ManyToMany(mappedBy = "sensors")
    @JsonIgnoreProperties({"sensors"})
	private List<Device> devices;
}

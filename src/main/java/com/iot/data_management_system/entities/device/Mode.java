package com.iot.data_management_system.entities.device;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="modes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Mode {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
	private int id;
	
	@Column(name="mode")
	private String mode;
	
}

package com.iot.data_management_system.dataAccess.device;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iot.data_management_system.entities.device.Mode;

public interface ModeRepository extends JpaRepository<Mode, Integer> {
	
	Mode findByMode(String mode);
	boolean existsByMode(String mode);
}

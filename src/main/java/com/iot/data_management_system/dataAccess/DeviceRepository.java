package com.iot.data_management_system.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;
import com.iot.data_management_system.entities.Device;

public interface DeviceRepository extends JpaRepository<Device, Integer> {

	Device findBySerialNumber(String num);

	boolean existsByName(String name);

	boolean existsBySerialNumber(String num);

}

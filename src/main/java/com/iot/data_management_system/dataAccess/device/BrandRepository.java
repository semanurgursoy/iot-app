package com.iot.data_management_system.dataAccess.device;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iot.data_management_system.entities.device.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
	
	Brand findByBrand(String brand);
	boolean existsByBrand(String brand);
	
}

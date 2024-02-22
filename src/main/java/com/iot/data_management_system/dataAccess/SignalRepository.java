package com.iot.data_management_system.dataAccess;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iot.data_management_system.entities.Signal;

public interface SignalRepository extends JpaRepository<Signal, Integer>{
	List<Signal> findByDeviceId(int id);
}

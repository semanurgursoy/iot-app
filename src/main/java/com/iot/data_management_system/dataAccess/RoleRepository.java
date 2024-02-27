package com.iot.data_management_system.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iot.data_management_system.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	Role findByRole(String role);
	boolean existsByRole(String role);
	
}

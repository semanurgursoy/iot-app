package com.iot.data_management_system.dataAccess;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iot.data_management_system.entities.Role;
import com.iot.data_management_system.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findByEmail(String email);
	List<User> findByRole(Role role);
	boolean existsByEmail(String email);
	User getReferenceByEmail(String email);
	
}

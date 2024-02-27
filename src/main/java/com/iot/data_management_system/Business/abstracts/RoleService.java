package com.iot.data_management_system.Business.abstracts;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.iot.data_management_system.entities.Role;
import com.iot.data_management_system.entities.dto.RoleDto;

public interface RoleService {
	
	List<RoleDto> getAll();
	RoleDto getByRole(String role);
	
	ResponseEntity<HttpStatus> add(Role role);
    ResponseEntity<HttpStatus> update(String role, String newRole);
    ResponseEntity<HttpStatus> delete(int id);

}

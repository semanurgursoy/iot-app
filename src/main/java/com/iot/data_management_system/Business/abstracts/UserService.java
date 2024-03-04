package com.iot.data_management_system.Business.abstracts;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.iot.data_management_system.entities.User;
import com.iot.data_management_system.entities.dto.UserDto;

public interface UserService {
	
	ResponseEntity<List<UserDto>> getAll();
	ResponseEntity<List<UserDto>> getByRole(String role);
	ResponseEntity<UserDto> getByEmail(String email);
	User findByEmail(String email);
	
	ResponseEntity<HttpStatus> add(UserDto dto);
    ResponseEntity<HttpStatus> update(UserDto dto);
    ResponseEntity<HttpStatus> delete(int id);

}

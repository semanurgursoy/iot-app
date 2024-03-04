package com.iot.data_management_system.WebAPIs;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.data_management_system.Business.abstracts.UserService;
import com.iot.data_management_system.entities.dto.UserDto;

@RestController
@RequestMapping("/api/users")
public class UsersController {
	
	private UserService userService;

	public UsersController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/getall")
	public ResponseEntity<List<UserDto>> getAll(){
		return userService.getAll();
	}
	
	@GetMapping("/getall_by_role")
	public ResponseEntity<List<UserDto>> getAllByRole(String role){
		return userService.getByRole(role);
	}
	
	@GetMapping("/get_by_email")
	public ResponseEntity<UserDto> getByEmail(String email){
		return userService.getByEmail(email);
	}
	
	@PostMapping("/add")
	public ResponseEntity<HttpStatus> add(@RequestBody UserDto dto){
		return userService.add(dto);
	}
	
	@PostMapping("/update")
	public ResponseEntity<HttpStatus> update(@RequestBody UserDto dto){
		return userService.update(dto);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<HttpStatus> delete(int id){
		return userService.delete(id);
	}
}

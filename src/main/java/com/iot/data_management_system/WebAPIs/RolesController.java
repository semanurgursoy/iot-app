package com.iot.data_management_system.WebAPIs;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.data_management_system.Business.abstracts.RoleService;
import com.iot.data_management_system.entities.dto.RoleDto;

@RestController
@RequestMapping("/api/roles")
public class RolesController {

	private RoleService roleService;

	public RolesController(RoleService roleService) {
		this.roleService = roleService;
	}
	
	@GetMapping("/getall")
	public ResponseEntity<List<RoleDto>> getAll(){
		return roleService.getAll();
	}
	
	@GetMapping("/get_by_role")
	public ResponseEntity<RoleDto> getByRole(String role){
		return roleService.getByRole(role);
	}
	
	@PostMapping("/add")
	public ResponseEntity<HttpStatus> add(@RequestBody RoleDto roleDto){
		return roleService.add(roleDto);
	}
	
	@PostMapping("/update")
	public ResponseEntity<HttpStatus> update(String role, String newRole){
		return roleService.update(role, newRole);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<HttpStatus> delete(int id){
		return roleService.delete(id);
	}
}

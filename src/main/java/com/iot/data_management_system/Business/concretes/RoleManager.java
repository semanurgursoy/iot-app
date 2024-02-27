package com.iot.data_management_system.Business.concretes;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iot.data_management_system.Business.abstracts.RoleService;
import com.iot.data_management_system.dataAccess.RoleRepository;
import com.iot.data_management_system.entities.Role;
import com.iot.data_management_system.entities.dto.RoleDto;

@Service
public class RoleManager implements RoleService {

	private RoleRepository roleRepository;
	private ModelMapper modelMapper;
	
	public RoleManager(RoleRepository roleRepository, ModelMapper modelMapper) {
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<RoleDto> getAll() {
		List<Role> roleList = roleRepository.findAll();
		List<RoleDto> dtoList = new ArrayList<RoleDto>();
		for(Role role: roleList)
			dtoList.add(modelMapper.map(role, RoleDto.class));
		return dtoList;
	}

	@Override
	public RoleDto getByRole(String role) {
		Role roleObject = roleRepository.findByRole(role);
		RoleDto dto = modelMapper.map(roleObject, RoleDto.class);
		return dto;
	}

	@Override
	public ResponseEntity<HttpStatus> add(Role role) {
		if(!roleRepository.existsByRole(role.getRole())) {
			roleRepository.save(role);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<HttpStatus> update(String role, String newRole) {
		if(roleRepository.existsByRole(role)) {
			Role r = roleRepository.findByRole(role);
			r.setRole(newRole);
			roleRepository.save(r);
			
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<HttpStatus> delete(int id) {
		if(roleRepository.existsById(id)) {
			roleRepository.deleteById(id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

}

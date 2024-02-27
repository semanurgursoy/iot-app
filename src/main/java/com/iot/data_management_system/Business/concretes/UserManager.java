package com.iot.data_management_system.Business.concretes;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iot.data_management_system.Business.Utils;
import com.iot.data_management_system.Business.abstracts.UserService;
import com.iot.data_management_system.dataAccess.RoleRepository;
import com.iot.data_management_system.dataAccess.UserRepository;
import com.iot.data_management_system.entities.Role;
import com.iot.data_management_system.entities.User;
import com.iot.data_management_system.entities.dto.UserDto;

@Service
public class UserManager implements UserService {
	
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private ModelMapper modelMapper;

	public UserManager(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<UserDto> getAll() {
		List<User> userList = userRepository.findAll();
		List<UserDto> dtoList = new ArrayList<UserDto>();
		for(User user: userList)
			dtoList.add(modelMapper.map(user, UserDto.class));
		return dtoList;
	}

	@Override
	public List<UserDto> getByRole(String role) {
		Role roleObject = roleRepository.findByRole(role);
		List<User> userList = userRepository.findByRole(roleObject);
		List<UserDto> dtoList = new ArrayList<UserDto>();
		for(User user: userList)
			dtoList.add(modelMapper.map(user, UserDto.class));
		return dtoList;
	}

	@Override
	public UserDto getByEmail(String email) {
		User user = userRepository.findByEmail(email);
		UserDto dto = modelMapper.map(user, UserDto.class);
		return dto;
	}

	@Override
	public ResponseEntity<HttpStatus> add(UserDto dto) {
		if(!userRepository.existsByEmail(dto.getEmail()) && roleRepository.existsByRole(dto.getRole())) {
			User user = new User();
			user.setRole(roleRepository.findByRole(dto.getRole()));
			BeanUtils.copyProperties(dto, user, "id", "role");
			userRepository.save(user);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
		
	}

	@Override
	public ResponseEntity<HttpStatus> update(UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
        	User user = userRepository.findByEmail(dto.getEmail());
        	if(!dto.getRole().isEmpty() && roleRepository.existsByRole(dto.getRole())) {
        		user.setRole(roleRepository.findByRole(dto.getRole()));
        	}
            BeanUtils.copyProperties(dto, user, Utils.getNullPropertyNames(dto));

            userRepository.save(user);
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        }
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<HttpStatus> delete(int id) {
		if(userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}
	

	

}

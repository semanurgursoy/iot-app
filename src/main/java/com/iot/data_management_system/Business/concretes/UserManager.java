package com.iot.data_management_system.Business.concretes;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private final PasswordEncoder encoder;
	
	public UserManager(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
		this.encoder = encoder;
	}

	@Override
	public ResponseEntity<List<UserDto>> getAll() {
		List<User> userList = userRepository.findAll();
		List<UserDto> dtoList = new ArrayList<UserDto>();
		for(User user: userList)
			dtoList.add(modelMapper.map(user, UserDto.class));
		return new ResponseEntity<>(dtoList, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<UserDto>> getByRole(String role) {
		if(roleRepository.existsByRole(role)) {
			Role roleObject = roleRepository.findByRole(role);
			List<User> userList = userRepository.findByRole(roleObject);
			List<UserDto> dtoList = new ArrayList<UserDto>();
			for(User user: userList)
				dtoList.add(modelMapper.map(user, UserDto.class));
			return new ResponseEntity<>(dtoList, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<UserDto> getByEmail(String email) {
		if(userRepository.existsByEmail(email)) {
			User user = userRepository.findByEmail(email);
			UserDto dto = modelMapper.map(user, UserDto.class);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public ResponseEntity<HttpStatus> add(UserDto dto) {
		if(!userRepository.existsByEmail(dto.getEmail()) && roleRepository.existsByRole(dto.getRole())) {
			User user = new User();
			user.setRole(roleRepository.findByRole(dto.getRole()));
			user.setPassword(encoder.encode(dto.getPassword()));
			BeanUtils.copyProperties(dto, user, "id", "role", "password");
			userRepository.save(user);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
		
	}

	@Override
	public ResponseEntity<HttpStatus> update(UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
        	User user = userRepository.findByEmail(dto.getEmail());
        	if(dto.getRole() != null && roleRepository.existsByRole(dto.getRole())) 
        		user.setRole(roleRepository.findByRole(dto.getRole()));       	
        	if(dto.getPassword() != null) { 
        		user.setPassword(encoder.encode(dto.getPassword()));
        		dto.setPassword(null);
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

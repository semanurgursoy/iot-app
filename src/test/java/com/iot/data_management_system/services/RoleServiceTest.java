package com.iot.data_management_system.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.iot.data_management_system.Business.concretes.RoleManager;
import com.iot.data_management_system.dataAccess.RoleRepository;
import com.iot.data_management_system.entities.Role;
import com.iot.data_management_system.entities.dto.RoleDto;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private RoleManager underTest;

	@Test
	void getAll_shouldReturnOkAndListOfRoleDto() {
		// Arrange
		List<Role> mockRoleList = Arrays.asList(new Role(1, "ADMIN"), new Role(2, "USER") 
		);

		List<RoleDto> mockRoleDtoList = new ArrayList<RoleDto>();
		for (int i = 0; i < mockRoleList.size(); i++) {
			mockRoleDtoList.add(new RoleDto());
			BeanUtils.copyProperties(mockRoleList.get(i), mockRoleDtoList.get(i));
		}
 
		when(roleRepository.findAll()).thenReturn(mockRoleList);
		for (int i = 0; i < mockRoleList.size(); i++)
			when(modelMapper.map(mockRoleList.get(i), RoleDto.class)).thenReturn(mockRoleDtoList.get(i));

		// Act
		ResponseEntity<List<RoleDto>> response = underTest.getAll();

		// Assert
		for (int i = 0; i < response.getBody().size(); i++)
			assertEquals(mockRoleList.get(i).getRole(), response.getBody().get(i).getRole());
	}

	@Test
	void getByRole_shouldReturnOkAndRoleDtoWhenRoleExists() {
		// Arrange
		String existingRole = "ADMIN";
		Role mockRole = new Role(1, existingRole);
		RoleDto mockRoleDto = new RoleDto();
		mockRoleDto.setRole(existingRole);

		when(roleRepository.existsByRole(existingRole)).thenReturn(true);
		when(roleRepository.findByRole(existingRole)).thenReturn(mockRole);
		when(modelMapper.map(mockRole, RoleDto.class)).thenReturn(mockRoleDto);

		// Act
		ResponseEntity<RoleDto> response = underTest.getByRole(existingRole);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody().getRole(), existingRole);
	}

	@Test
	void getByRole_shouldReturnNotFoundWhenRoleDoesNotExist() {
		// Arrange
		String nonExistentRole = "UNKNOWN_ROLE";

		when(roleRepository.existsByRole(nonExistentRole)).thenReturn(false);

		// Act
		ResponseEntity<RoleDto> response = underTest.getByRole(nonExistentRole);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(response.getBody(), null); 
	}

	@Test
	void add_shouldSaveNewRoleAndReturnOkWhenRoleDoesNotExist() {
		// Arrange
		RoleDto roleDto = new RoleDto();
		roleDto.setRole("NEW_ROLE");

		when(roleRepository.existsByRole(roleDto.getRole())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(roleDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(roleRepository).save(any(Role.class));
	}

	@Test
	void add_shouldReturnBadRequestWhenRoleAlreadyExists() {
		// Arrange
		RoleDto roleDto = new RoleDto();
		roleDto.setRole("EXISTING_ROLE");

		when(roleRepository.existsByRole(roleDto.getRole())).thenReturn(true);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(roleDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(roleRepository, never()).save(any(Role.class));
	}

	@Test
	void update_shouldReturnOkWhenRoleExistsAndUpdateRole() {
		// Arrange
		String existingRole = "ADMIN";
		String newRole = "EDITOR";

		Role existingRoleObject = new Role();
		existingRoleObject.setRole(existingRole);

		when(roleRepository.existsByRole(existingRole)).thenReturn(true);
		when(roleRepository.findByRole(existingRole)).thenReturn(existingRoleObject);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(existingRole, newRole);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(roleRepository).save(existingRoleObject);
	}

	@Test
	void update_shouldReturnNotFoundWhenRoleDoesNotExist() {
		// Arrange
		String existingRole = "NON_EXISTENT_ROLE";
		String newRole = "EDITOR";

		when(roleRepository.existsByRole(existingRole)).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(existingRole, newRole);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(roleRepository, never()).save(any(Role.class));
	}

	@Test
	void delete_shouldReturnOkWhenRoleExists() {
		// Arrange
		int existingId = 1;

		when(roleRepository.existsById(existingId)).thenReturn(true);

		// Act
		ResponseEntity<HttpStatus> response = underTest.delete(existingId);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(roleRepository).deleteById(existingId); 
	}

	@Test
	void delete_shouldReturnNotFoundWhenRoleDoesNotExist() {
		// Arrange
		int nonExistentId = 100;

		when(roleRepository.existsById(nonExistentId)).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.delete(nonExistentId);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(roleRepository, never()).deleteById(anyInt()); 
	}

}

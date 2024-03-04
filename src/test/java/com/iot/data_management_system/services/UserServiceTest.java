package com.iot.data_management_system.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.iot.data_management_system.Business.concretes.UserManager;
import com.iot.data_management_system.dataAccess.RoleRepository;
import com.iot.data_management_system.dataAccess.UserRepository;
import com.iot.data_management_system.entities.Role;
import com.iot.data_management_system.entities.User;
import com.iot.data_management_system.entities.dto.UserDto;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private ModelMapper modelMapper;

	@Mock
	private PasswordEncoder encoder;

	@InjectMocks
	private UserManager underTest;

	User testUser1 = new User(1, "User1", "User1", "user1@gmail.com", "user1-123", new Role(1, "USER"));
	User testUser2 = new User(1, "Admin1", "Admin1", "admin1@gmail.com", "admin1-123", new Role(2, "ADMIN"));
	UserDto testUserDto1 = new UserDto("User1", "User1", "user1@gmail.com", "user1-123", "USER");
	UserDto testUserDto2 = new UserDto("Admin1", "Admin1", "admin1@gmail.com", "admin1-123", "ADMIN");

	@Test
	void getAll_shouldReturnOkAndListOfUserDto() {
		// Arrange
		List<User> mockUserList = Arrays.asList(testUser1, testUser2);

		List<UserDto> mockUserDtoList = Arrays.asList(testUserDto1, testUserDto2);

		when(userRepository.findAll()).thenReturn(mockUserList);
		for (int i = 0; i < mockUserList.size(); i++)
			when(modelMapper.map(mockUserList.get(i), UserDto.class)).thenReturn(mockUserDtoList.get(i));

		// Act
		ResponseEntity<List<UserDto>> response = underTest.getAll();

		// Assert
		for (int i = 0; i < response.getBody().size(); i++) {
			assertEquals(mockUserList.get(i).getName(), response.getBody().get(i).getName());
			assertEquals(mockUserList.get(i).getSurname(), response.getBody().get(i).getSurname());
			assertEquals(mockUserList.get(i).getEmail(), response.getBody().get(i).getEmail());
			assertEquals(mockUserList.get(i).getPassword(), response.getBody().get(i).getPassword());
			assertEquals(mockUserList.get(i).getRole().getRole(), response.getBody().get(i).getRole());
		}
	}

	@Test
	void getByRole_shouldReturnOkAndListOfUserDtosWhenRoleExists() {
		// Arrange
		String existingRole = "ADMIN";
		Role mockRole = new Role(1, existingRole);
		List<User> mockUserList = Arrays.asList(new User(), new User());
		List<UserDto> expectedDtoList = Arrays.asList(new UserDto(), new UserDto());

		when(roleRepository.existsByRole(existingRole)).thenReturn(true);
		when(roleRepository.findByRole(existingRole)).thenReturn(mockRole);
		when(userRepository.findByRole(mockRole)).thenReturn(mockUserList);
		when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(new UserDto());

		// Act
		ResponseEntity<List<UserDto>> response = underTest.getByRole(existingRole);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDtoList, response.getBody());

	}

	@Test
	void getByRole_shouldReturnNotFoundWhenRoleDoesNotExist() {
		// Arrange
		String nonExistentRole = "UNKNOWN_ROLE";

		when(roleRepository.existsByRole(nonExistentRole)).thenReturn(false);

		// Act
		ResponseEntity<List<UserDto>> response = underTest.getByRole(nonExistentRole);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	void getByEmail_shouldReturnOkAndUserDtoWhenEmailExists() {
		// Arrange
		String existingEmail = "user1@gmail.com";
		User mockUser = testUser1;
		UserDto expectedDto = testUserDto1;

		when(userRepository.existsByEmail(existingEmail)).thenReturn(true);
		when(userRepository.findByEmail(existingEmail)).thenReturn(mockUser);
		when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(expectedDto);

		// Act
		ResponseEntity<UserDto> response = underTest.getByEmail(existingEmail);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDto, response.getBody());
	}

	@Test
	void getByEmail_shouldReturnNotFoundWhenEmailDoesNotExist() {
		// Arrange
		String nonExistentEmail = "unknown@example.com";

		when(userRepository.existsByEmail(nonExistentEmail)).thenReturn(false);

		// Act
		ResponseEntity<UserDto> response = underTest.getByEmail(nonExistentEmail);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	void findByEmail_shouldCallUserRepositoryWhenInvoked() {
		// Arrange
		String testEmail = "user1@gmail.com";

		when(userRepository.findByEmail(testEmail)).thenReturn(testUser1);

		// Act
		User response = underTest.findByEmail(testEmail);

		// Assert
		assertEquals(testEmail, response.getEmail());
	}

	@Test
	void add_shouldReturnOkWhenUserCreatedSuccessfully() {
		// Arrange
		UserDto validDto = testUserDto1;
		Role mockRole = new Role(1, "USER");

		when(userRepository.existsByEmail(validDto.getEmail())).thenReturn(false);
		when(roleRepository.existsByRole(validDto.getRole())).thenReturn(true);
		when(roleRepository.findByRole(validDto.getRole())).thenReturn(mockRole);
		when(encoder.encode(validDto.getPassword())).thenReturn("hashedPassword");

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(validDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void add_shouldReturnBadRequestWhenRoleDoesNotExist() {
		// Arrange
		UserDto dto = testUserDto1;

		when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
		when(roleRepository.existsByRole(dto.getRole())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(dto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void update_shouldReturnOkWhenUserUpdatedSuccessfully() {
		// Arrange
		UserDto updateDto = testUserDto1;
		User existingUser = testUser1;

		when(userRepository.existsByEmail(updateDto.getEmail())).thenReturn(true);
		when(userRepository.findByEmail(updateDto.getEmail())).thenReturn(existingUser);
		when(roleRepository.existsByRole(updateDto.getRole())).thenReturn(true);
		when(roleRepository.findByRole(updateDto.getRole())).thenReturn(new Role(1, "ADMIN"));
		when(encoder.encode(updateDto.getPassword())).thenReturn("hashedPassword");

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(updateDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	void update_shouldReturnNotFoundWhenEmailDoesNotExist() {
		// Arrange
		UserDto updateDto = testUserDto1;

		when(userRepository.existsByEmail(updateDto.getEmail())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(updateDto);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void delete_shouldReturnOkWhenUserIsDeleted() {
		// Arrange
		int existingId = 1;

		when(userRepository.existsById(existingId)).thenReturn(true);

		// Act
		ResponseEntity<HttpStatus> response = underTest.delete(existingId);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(userRepository).deleteById(existingId);
	}

	@Test
	void delete_shouldReturnNotFoundWhenUserDoesNotExist() {
		// Arrange
		int nonExistentId = 10;

		when(userRepository.existsById(nonExistentId)).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.delete(nonExistentId);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

}

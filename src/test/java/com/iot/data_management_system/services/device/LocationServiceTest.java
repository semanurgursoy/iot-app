package com.iot.data_management_system.services.device;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
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

import com.iot.data_management_system.Business.concretes.device.LocationManager;
import com.iot.data_management_system.dataAccess.device.LocationRepository;
import com.iot.data_management_system.entities.device.Location;
import com.iot.data_management_system.entities.dto.LocationDto;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {

	@Mock
	private LocationRepository locationRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private LocationManager underTest;

	Location testLocation1 = new Location(1, "Türkiye", "İstanbul", "İstiklal Caddesi", 41.0082, 28.9784);
	Location testLocation2 = new Location(2, "Japonya", "Tokyo", "Shibuya Crossing", 35.6895, 139.6917);
	LocationDto testLocationDto1 = new LocationDto("Türkiye", "İstanbul", "İstiklal Caddesi", 41.0082, 28.9784);
	LocationDto testLocationDto2 = new LocationDto("Japonya", "Tokyo", "Shibuya Crossing", 35.6895, 139.6917);

	@Test
	void getAll_shouldReturnOkAndListOfLocationDtosWhenLocationsExist() {
		// Arrange
		List<Location> mockLocationList = Arrays.asList(testLocation1, testLocation2);
		List<LocationDto> expectedDtoList = Arrays.asList(testLocationDto1, testLocationDto2);

		when(locationRepository.findAll()).thenReturn(mockLocationList);
		for (int i = 0; i < mockLocationList.size(); i++)
			when(modelMapper.map(mockLocationList.get(i), LocationDto.class)).thenReturn(expectedDtoList.get(i));

		// Act
		ResponseEntity<List<LocationDto>> response = underTest.getAll();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDtoList, response.getBody());
	}

	@Test
	void add_shouldReturnOkWhenLocationAddedSuccessfully() {
		// Arrange
		LocationDto validDto = testLocationDto1;

		when(locationRepository.exists(anyDouble(), anyDouble())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(validDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(locationRepository).save(any(Location.class));
	}

	@Test
	void add_shouldReturnBadRequestWhenLocationAlreadyExists() {
		// Arrange
		LocationDto duplicateLocationDto = testLocationDto1;

		when(locationRepository.exists(anyDouble(), anyDouble())).thenReturn(true);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(duplicateLocationDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(locationRepository, never()).save(any(Location.class));
	}

	@Test
	void update_shouldReturnOkWhenLocationUpdatedSuccessfully() {
		// Arrange
		LocationDto validDto = testLocationDto1;
		double longitude = 41.0082, latitude = 28.9784;
		Location mockLocation = testLocation1;

		when(locationRepository.exists(longitude, latitude)).thenReturn(true);
		when(locationRepository.findByLocation(longitude, latitude)).thenReturn(mockLocation);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(validDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(locationRepository).save(mockLocation);
	}

	@Test
	void update_shouldReturnNotFoundWhenLocationDoesNotExist() {
		// Arrange
		LocationDto dto = testLocationDto1;

		when(locationRepository.exists(anyDouble(), anyDouble())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(dto);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(locationRepository, never()).findByLocation(anyDouble(), anyDouble());
		verify(locationRepository, never()).save(any(Location.class));
	}

	@Test
	void delete_shouldReturnOkWhenLocationIsDeleted() {
		// Arrange
		int existingId = 1;

		when(locationRepository.existsById(existingId)).thenReturn(true);

		// Act
		ResponseEntity<HttpStatus> response = underTest.delete(existingId);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(locationRepository).deleteById(existingId);
	}

	@Test
	void delete_shouldReturnNotFoundWhenLocationDoesNotExist() {
		// Arrange
		int nonExistentId = 10;

		when(locationRepository.existsById(nonExistentId)).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.delete(nonExistentId);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(locationRepository, never()).deleteById(anyInt());
	}
}

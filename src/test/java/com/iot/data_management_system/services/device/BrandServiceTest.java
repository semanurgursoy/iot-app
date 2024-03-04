package com.iot.data_management_system.services.device;

import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

import com.iot.data_management_system.Business.concretes.device.BrandManager;
import com.iot.data_management_system.dataAccess.device.BrandRepository;
import com.iot.data_management_system.entities.device.Brand;
import com.iot.data_management_system.entities.dto.BrandDto;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {

	@Mock
	private BrandRepository brandRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private BrandManager underTest;

	Brand testBrand1 = new Brand(1, "Xiaomi");
	Brand testBrand2 = new Brand(2, "Samsung");
	BrandDto testBrandDto1 = new BrandDto("Xiaomi");
	BrandDto testBrandDto2 = new BrandDto("Samsung");

	@Test
	void getAll_shouldReturnOkAndListOfBrandDtosWhenBrandsExist() {
		// Arrange
		List<Brand> mockBrandList = Arrays.asList(testBrand1, testBrand2);
		List<BrandDto> expectedDtoList = Arrays.asList(testBrandDto1, testBrandDto2);

		when(brandRepository.findAll()).thenReturn(mockBrandList);
		for (int i = 0; i < mockBrandList.size(); i++)
			when(modelMapper.map(mockBrandList.get(i), BrandDto.class)).thenReturn(expectedDtoList.get(i));

		// Act
		ResponseEntity<List<BrandDto>> response = underTest.getAll();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDtoList, response.getBody());
	}

	@Test
	void getByName_shouldReturnOkAndBrandDtoWhenBrandExists() {
		// Arrange
		String existingBrand = "Xiaomi";
		Brand mockBrand = testBrand1;
		BrandDto expectedDto = testBrandDto1;

		when(brandRepository.existsByBrand(existingBrand)).thenReturn(true);
		when(brandRepository.findByBrand(existingBrand)).thenReturn(mockBrand);
		when(modelMapper.map(any(Brand.class), eq(BrandDto.class))).thenReturn(expectedDto);

		// Act
		ResponseEntity<BrandDto> response = underTest.getByName(existingBrand);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDto, response.getBody());
	}

	@Test
	void getByName_shouldReturnNotFoundWhenBrandDoesNotExist() {
		// Arrange
		String nonExistentBrand = "UnknownBrand";

		when(brandRepository.existsByBrand(nonExistentBrand)).thenReturn(false);

		// Act
		ResponseEntity<BrandDto> response = underTest.getByName(nonExistentBrand);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	void add_shouldReturnOkWhenBrandAddedSuccessfully() {
		// Arrange
		BrandDto validDto = testBrandDto1;

		when(brandRepository.existsByBrand(validDto.getBrand())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(validDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(brandRepository).save(any(Brand.class));
	}

	@Test
	void add_shouldReturnBadRequestWhenBrandAlreadyExists() {
		// Arrange
		BrandDto duplicateBrandDto = testBrandDto1;

		when(brandRepository.existsByBrand(duplicateBrandDto.getBrand())).thenReturn(true);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(duplicateBrandDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(brandRepository, never()).save(any(Brand.class));
	}

	@Test
	void update_shouldReturnOkWhenBrandUpdatedSuccessfully() {
		// Arrange
		String existingBrand = "Xiaomi";
		String newBrand = "Intel";
		Brand mockBrand = testBrand1;

		when(brandRepository.existsByBrand(existingBrand)).thenReturn(true);
		when(brandRepository.findByBrand(existingBrand)).thenReturn(mockBrand);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(existingBrand, newBrand);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(newBrand, mockBrand.getBrand());
		verify(brandRepository).save(mockBrand);
	}

	@Test
	void update_shouldReturnNotFoundWhenBrandDoesNotExist() {
		// Arrange
		String nonExistentBrand = "UnknownBrand";
		String newBrand = "NewBrand";

		when(brandRepository.existsByBrand(nonExistentBrand)).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(nonExistentBrand, newBrand);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(brandRepository, never()).save(any(Brand.class));
	}

	@Test
	void delete_shouldReturnOkWhenBrandIsDeleted() {
		// Arrange
		int existingId = 1;

		when(brandRepository.existsById(existingId)).thenReturn(true);

		// Act
		ResponseEntity<HttpStatus> response = underTest.delete(existingId);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(brandRepository).deleteById(existingId);
	}

	@Test
	void delete_shouldReturnNotFoundWhenBrandDoesNotExist() {
		// Arrange
		int nonExistentId = 10;

		when(brandRepository.existsById(nonExistentId)).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.delete(nonExistentId);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(brandRepository, never()).deleteById(anyInt());
	}

}

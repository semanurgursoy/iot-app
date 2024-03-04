package com.iot.data_management_system.services.device;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.iot.data_management_system.Business.concretes.device.SensorManager;
import com.iot.data_management_system.dataAccess.device.BrandRepository;
import com.iot.data_management_system.dataAccess.device.ModeRepository;
import com.iot.data_management_system.dataAccess.device.SensorRepository;
import com.iot.data_management_system.entities.device.Brand;
import com.iot.data_management_system.entities.device.Mode;
import com.iot.data_management_system.entities.device.Sensor;
import com.iot.data_management_system.entities.dto.SensorDto;

@ExtendWith(MockitoExtension.class)
public class SensorServiceTest {

	@Mock
	private SensorRepository sensorRepository;

	@Mock
	private ModeRepository modeRepository;

	@Mock
	private BrandRepository brandRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private SensorManager underTest;

	Sensor testSensor1 = new Sensor(1, "Sıcaklık Sensörü", "TH-100", "ABC123", "Kablosuz", "Ortam sıcaklığını ölçmek",
			"100g", "%100", "1 yıl", "-20°C - 50°C", "1 saniye", true, true, true, false, true, new Mode(1, "normal"),
			new Brand(1, "ACME"), new ArrayList<>());

	Sensor testSensor2 = new Sensor(2, "Nem Sensörü", "HS-200", "XYZ456", "Kablolu", "Ortam nemini ölçmek", "50g",
			"%70", "2 yıl", "%10 - %90", "2 saniye", true, true, false, false, false, new Mode(2, "airplane"),
			new Brand(1, "ACME"), new ArrayList<>());

	SensorDto testSensorDto1 = new SensorDto("Sıcaklık Sensörü", "TH-100", "ABC123", "Kablosuz",
			"Ortam sıcaklığını ölçmek", "100g", "%100", "1 yıl", "-20°C - 50°C", "1 saniye", true, true, true, false,
			true, "normal", "ACME");

	SensorDto testSensorDto2 = new SensorDto("Nem Sensörü", "HS-200", "XYZ456", "Kablolu", "Ortam nemini ölçmek", "50g",
			"%70", "2 yıl", "%10 - %90", "2 saniye", true, true, false, false, false, "hassas", "BETA");

	@Test
	void getAll_shouldReturnOkAndListOfSensorDtosWhenSensorsExist() {
		// Arrange
		List<Sensor> mockSensorList = Arrays.asList(testSensor1, testSensor2);
		List<SensorDto> expectedDtoList = Arrays.asList(testSensorDto1, testSensorDto2);

		when(sensorRepository.findAll()).thenReturn(mockSensorList);
		for (int i = 0; i < mockSensorList.size(); i++)
			when(modelMapper.map(mockSensorList.get(i), SensorDto.class)).thenReturn(expectedDtoList.get(i));

		// Act
		ResponseEntity<List<SensorDto>> response = underTest.getAll();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDtoList, response.getBody());
	}

	@Test
	void getByName_shouldReturnOkAndListOfSensorDtosWhenSensorsFoundByName() {
		// Arrange
		String name = "Sıcaklık Sensörü";
		List<Sensor> mockSensorList = Arrays.asList(testSensor1, testSensor2);
		List<SensorDto> expectedDtoList = Arrays.asList(testSensorDto1, testSensorDto2);

		when(sensorRepository.existsByName(name)).thenReturn(true);
		when(sensorRepository.findByName(name)).thenReturn(mockSensorList);
		for (int i = 0; i < mockSensorList.size(); i++)
			when(modelMapper.map(mockSensorList.get(i), SensorDto.class)).thenReturn(expectedDtoList.get(i));

		// Act
		ResponseEntity<List<SensorDto>> response = underTest.getByName(name);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDtoList, response.getBody());
	}

	@Test
	void getByName_shouldReturnNotFoundWhenSensorsNotFoundByName() {
		// Arrange
		String name = "NonExistentSensor";
		when(sensorRepository.existsByName(name)).thenReturn(false);

		// Act
		ResponseEntity<List<SensorDto>> response = underTest.getByName(name);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	void getBySerialNumber_shouldReturnOkAndSensorDtoWhenSensorFoundBySerialNumber() {
		// Arrange
		String serialNumber = "ABC123";
		Sensor mockSensor = new Sensor();
		SensorDto expectedDto = new SensorDto();

		when(sensorRepository.existsBySerialNumber(serialNumber)).thenReturn(true);
		when(sensorRepository.findBySerialNumber(serialNumber)).thenReturn(mockSensor);
		when(modelMapper.map(mockSensor, SensorDto.class)).thenReturn(expectedDto);

		// Act
		ResponseEntity<SensorDto> response = underTest.getBySerialNumber(serialNumber);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDto, response.getBody());
	}

	@Test
	void getBySerialNumber_shouldReturnNotFoundWhenSensorNotFoundBySerialNumber() {
		// Arrange
		String serialNumber = "XYZ789";
		when(sensorRepository.existsBySerialNumber(serialNumber)).thenReturn(false);

		// Act
		ResponseEntity<SensorDto> response = underTest.getBySerialNumber(serialNumber);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	void add_shouldReturnOkWhenSensorAddedSuccessfully() {
		// Arrange
		SensorDto sensorDto = testSensorDto1;
		Mode mockMode = new Mode(1, "normal");
		Brand mockBrand = new Brand(1, "ACME");

		when(sensorRepository.existsBySerialNumber(anyString())).thenReturn(false);
		when(modeRepository.existsByMode(anyString())).thenReturn(true);
		when(modeRepository.findByMode(anyString())).thenReturn(mockMode);
		when(brandRepository.existsByBrand(anyString())).thenReturn(true);
		when(brandRepository.findByBrand(anyString())).thenReturn(mockBrand);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(sensorDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(sensorRepository).save(any(Sensor.class));
	}

	@Test
	void add_shouldReturnBadRequestWhenSensorAlreadyExists() {
		// Arrange
		SensorDto sensorDto = testSensorDto1;
		when(sensorRepository.existsBySerialNumber(anyString())).thenReturn(true);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(sensorDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(sensorRepository, never()).save(any(Sensor.class));
	}

	@Test
	void add_shouldReturnBadRequestWhenModeOrBrandDoesNotExist() {
		// Arrange
		SensorDto sensorDto = testSensorDto1;
		when(sensorRepository.existsBySerialNumber(anyString())).thenReturn(false);
		when(modeRepository.existsByMode(anyString())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(sensorDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(sensorRepository, never()).save(any(Sensor.class));
	}

	@Test
	void update_shouldReturnOkWhenSensorUpdatedSuccessfully() {
		// Arrange
		SensorDto sensorDto = testSensorDto1;
		Sensor mockSensor = testSensor1;
		Mode mockMode = new Mode(2, "airplane");
		Brand mockBrand = new Brand(2, "XYZ");

		when(sensorRepository.existsBySerialNumber(anyString())).thenReturn(true);
		when(sensorRepository.findBySerialNumber(anyString())).thenReturn(mockSensor);
		when(modeRepository.existsByMode(anyString())).thenReturn(true);
		when(modeRepository.findByMode(anyString())).thenReturn(mockMode);
		when(brandRepository.existsByBrand(anyString())).thenReturn(true);
		when(brandRepository.findByBrand(anyString())).thenReturn(mockBrand);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(sensorDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockMode, mockSensor.getMode());
		assertEquals(mockBrand, mockSensor.getBrand());
		verify(sensorRepository).save(mockSensor);
	}

	@Test
	void update_shouldReturnNotFoundWhenSensorDoesNotExist() {
		// Arrange
		SensorDto sensorDto = testSensorDto1;
		when(sensorRepository.existsBySerialNumber(anyString())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(sensorDto);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(sensorRepository, never()).findBySerialNumber(anyString());
		verify(sensorRepository, never()).save(any(Sensor.class));
	}

	@Test
	void update_shouldReturnBadRequestWhenModeDoesNotExist() {
		// Arrange
		SensorDto sensorDto = testSensorDto1;
		sensorDto.setMode("InvalidMode");
		when(sensorRepository.existsBySerialNumber(anyString())).thenReturn(true);
		Mockito.lenient().when(modeRepository.existsByMode(anyString())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(sensorDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(sensorRepository, never()).save(any(Sensor.class));
	}

	@Test
	void update_shouldReturnBadRequestWhenBrandDoesNotExist() {
		// Arrange
		SensorDto sensorDto = testSensorDto1;
		sensorDto.setBrand("InvalidBrand");
		when(sensorRepository.existsBySerialNumber(anyString())).thenReturn(true);
		when(brandRepository.existsByBrand(anyString())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(sensorDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(sensorRepository, never()).save(any(Sensor.class));
	}

	@Test
	void delete_shouldReturnOkWhenSensorExists() {
		// Arrange
		int sensorId = 1;
		when(sensorRepository.existsById(sensorId)).thenReturn(true);

		// Act
		ResponseEntity<HttpStatus> response = underTest.delete(sensorId);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(sensorRepository).deleteById(sensorId);
	}

	@Test
	void delete_shouldReturnNotFoundWhenSensorDoesNotExist() {
		// Arrange
		int sensorId = 5;
		when(sensorRepository.existsById(sensorId)).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.delete(sensorId);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(sensorRepository, never()).deleteById(anyInt());
	}

}

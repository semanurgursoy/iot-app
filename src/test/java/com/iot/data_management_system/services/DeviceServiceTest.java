package com.iot.data_management_system.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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

import com.iot.data_management_system.Business.concretes.DeviceManager;
import com.iot.data_management_system.dataAccess.DeviceRepository;
import com.iot.data_management_system.dataAccess.device.BrandRepository;
import com.iot.data_management_system.dataAccess.device.LocationRepository;
import com.iot.data_management_system.dataAccess.device.ModeRepository;
import com.iot.data_management_system.dataAccess.device.SensorRepository;
import com.iot.data_management_system.entities.Device;
import com.iot.data_management_system.entities.device.*;
import com.iot.data_management_system.entities.dto.DeviceDto;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {
	@Mock
	private DeviceRepository deviceRepository;

	@Mock
	private ModeRepository modeRepository;

	@Mock
	private BrandRepository brandRepository;

	@Mock
	private SensorRepository sensorRepository;

	@Mock
	private LocationRepository locationRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private DeviceManager underTest;

	Sensor testSensor1 = new Sensor(1, "Sıcaklık Sensörü", "TH-100", "abc", "Kablosuz", "Ortam sıcaklığını ölçmek",
			"100g", "%100", "1 yıl", "-20°C - 50°C", "1 saniye", true, true, true, false, true, new Mode(2, "normal"),
			new Brand(1, "ACME"), new ArrayList<>());

	Sensor testSensor2 = new Sensor(2, "Nem Sensörü", "HS-200", "def", "Kablolu", "Ortam nemini ölçmek", "50g", "%70",
			"2 yıl", "%10 - %90", "2 saniye", true, true, false, false, false, new Mode(1, "airplane"),
			new Brand(1, "ACME"), new ArrayList<>());

	Device testDevice1 = new Device(1, "Cihaz 2", "Model Y", "SN654321", "Genel Kullanım", "2 Kg", "Lityum", "1 Yıl",
			null, new Brand(1, "ACME"), new Mode(1, "airplane"), Arrays.asList(testSensor1, testSensor2),
			new Location());

	Device testDevice2 = new Device(2, "Cihaz 3", "Model Z", "SN987654", "Hava Kalitesi Kontrolü", "3.5 Kg",
			"Lityum İyon", "2 Yıl", LocalDate.of(2023, 10, 15), new Brand(2, "xiaomi"), new Mode(2, "normal"),
			new ArrayList<Sensor>(), new Location());

	DeviceDto testDeviceDto1 = new DeviceDto("Cihaz 2", "Model Y", "SN654321", "Genel Kullanım", "2 Kg", "Lityum",
			"1 Yıl", null, "ACME", "airplane", 10.5, 34.2, "Konum bilgisi yok", Arrays.asList("abc", "def"));

	DeviceDto testDeviceDto2 = new DeviceDto("Cihaz 3", "Model Z", "SN987654", "Hava Kalitesi Kontrolü", "3.5 Kg",
			"Lityum İyon", "2 Yıl", LocalDate.of(2023, 10, 15), "normal", "xiomi", 35.4, 28.9, "İstanbul, Türkiye",
			new ArrayList<String>());

	@Test
	void getAll_shouldReturnAllDevicesAsDtos() {
		// Arrange
		List<Device> mockDevices = Arrays.asList(testDevice1, testDevice2);
		List<DeviceDto> mockDtos = Arrays.asList(testDeviceDto1, testDeviceDto2);
		when(deviceRepository.findAll()).thenReturn(mockDevices);
		for (int i = 0; i < mockDevices.size(); i++)
			when(modelMapper.map(mockDevices.get(i), DeviceDto.class)).thenReturn(mockDtos.get(i));

		// Act
		ResponseEntity<List<DeviceDto>> response = underTest.getAll();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockDtos, response.getBody());
		verify(deviceRepository).findAll();
		verify(modelMapper, times(2)).map(any(Device.class), eq(DeviceDto.class));
	}

	@Test
	public void getBySerialNumber_shouldReturnDeviceDtoWhenDeviceExists() {
		// Arrange
		Device device = testDevice1;
		DeviceDto deviceDto = testDeviceDto1;
		String serialNumber = "12345";

		// Act
		when(deviceRepository.existsBySerialNumber(serialNumber)).thenReturn(true);
		when(deviceRepository.findBySerialNumber(serialNumber)).thenReturn(device);
		when(modelMapper.map(device, DeviceDto.class)).thenReturn(deviceDto);

		ResponseEntity<DeviceDto> response = underTest.getBySerialNumber(serialNumber);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(deviceDto, response.getBody());
	}

	@Test
	public void getBySerialNumber_shouldReturnNotFoundWhenDeviceDoesNotExist() {
		// Arrange
		String serialNumber = "12345";

		// Act
		when(deviceRepository.existsBySerialNumber(serialNumber)).thenReturn(false);

		ResponseEntity<DeviceDto> response = underTest.getBySerialNumber(serialNumber);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	public void add_shouldAddDeviceAndReturnOkWhenDataIsValid() {
		// Arrange
		DeviceDto deviceDto = testDeviceDto1;

		when(deviceRepository.existsBySerialNumber(deviceDto.getSerialNumber())).thenReturn(false);
		when(modeRepository.existsByMode(deviceDto.getMode())).thenReturn(true);
		when(brandRepository.existsByBrand(deviceDto.getBrand())).thenReturn(true);
		when(sensorRepository.checkSensorsExists(deviceDto.getSensors())).thenReturn(true);
		when(locationRepository.exists(deviceDto.getLongitude(), deviceDto.getLatitude())).thenReturn(true);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(deviceDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(deviceRepository).save(any(Device.class));
	}

	@Test
	public void add_shouldReturnBadRequest_whenSerialNumberIsExist() {
		// Arrange
		DeviceDto deviceDto = testDeviceDto1;

		when(deviceRepository.existsBySerialNumber(deviceDto.getSerialNumber())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(deviceDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(deviceRepository, never()).save(any(Device.class));
	}

	@Test
	public void add_shouldReturnBadRequest_whenModeIsNotFound() {
		// Arrange
		DeviceDto deviceDto = testDeviceDto1;

		when(modeRepository.existsByMode(deviceDto.getMode())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(deviceDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(deviceRepository, never()).save(any(Device.class));
	}

	@Test
	public void add_shouldReturnBadRequest_whenBrandIsNotFound() {
		// Arrange
		DeviceDto deviceDto = testDeviceDto1;

		Mockito.lenient().when(brandRepository.existsByBrand(deviceDto.getBrand())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(deviceDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(deviceRepository, never()).save(any(Device.class));
	}

	@Test
	public void add_shouldReturnBadRequest_whenSensorsAreNotFound() {
		// Arrange
		DeviceDto deviceDto = testDeviceDto1;

		Mockito.lenient().when(sensorRepository.checkSensorsExists(deviceDto.getSensors())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(deviceDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(deviceRepository, never()).save(any(Device.class));
	}

	@Test
	public void add_shouldReturnBadRequest_whenLocationIsNotFound() {
		// Arrange
		DeviceDto deviceDto = testDeviceDto1;

		Mockito.lenient().when(locationRepository.exists(testDeviceDto1.getLongitude(), testDeviceDto1.getLatitude()))
				.thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(deviceDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(deviceRepository, never()).save(any(Device.class));
	}

	@Test
	public void update_shouldUpdateDeviceAndReturnOk_whenDeviceExists() {
		// Arrange
		DeviceDto deviceDto = testDeviceDto1;
		Device device = testDevice1;
		Mode mode = new Mode(1, "airplane");
		Brand brand = new Brand(1, "ACME");
		List<Sensor> sensors = Arrays.asList(testSensor1, testSensor2);
		Location location = new Location(1, "abc", "def", "xyz", 10.5, 34.2);

		when(deviceRepository.existsBySerialNumber(deviceDto.getSerialNumber())).thenReturn(true);
		when(deviceRepository.findBySerialNumber(deviceDto.getSerialNumber())).thenReturn(device);
		when(modeRepository.existsByMode(deviceDto.getMode())).thenReturn(true);
		when(modeRepository.findByMode(deviceDto.getMode())).thenReturn(mode);
		when(brandRepository.existsByBrand(deviceDto.getBrand())).thenReturn(true);
		when(brandRepository.findByBrand(deviceDto.getBrand())).thenReturn(brand);
		when(sensorRepository.checkSensorsExists(deviceDto.getSensors())).thenReturn(true);
		when(sensorRepository.convertTypeToSensor(deviceDto.getSensors())).thenReturn(sensors);
		when(locationRepository.exists(deviceDto.getLongitude(), deviceDto.getLatitude())).thenReturn(true);
		when(locationRepository.findByLocation(deviceDto.getLongitude(), deviceDto.getLatitude())).thenReturn(location);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(deviceDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mode, testDevice1.getMode());
		assertEquals(brand, testDevice1.getBrand());
		assertEquals(sensors, testDevice1.getSensors());
		assertEquals(location, testDevice1.getLocation());
		verify(deviceRepository).save(testDevice1);
	}

	@Test
	public void update_shouldReturnNotFound_whenDeviceDoesNotExist() {
		// Arrange
		DeviceDto deviceDto = testDeviceDto1;

		when(deviceRepository.existsBySerialNumber(deviceDto.getSerialNumber())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(deviceDto);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(deviceRepository, never()).save(any(Device.class));
	}

	@Test
	public void update_shouldReturnNotFound_whenModeDoesNotExist() {
		// Arrange
		DeviceDto deviceDto = testDeviceDto1;

		Mockito.lenient().when(modeRepository.existsByMode(deviceDto.getMode())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(deviceDto);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(deviceRepository, never()).save(any(Device.class));
	}

	@Test
	public void update_shouldReturnNotFound_whenBrandDoesNotExist() {
		// Arrange
		DeviceDto deviceDto = testDeviceDto1;

		Mockito.lenient().when(brandRepository.existsByBrand(deviceDto.getBrand())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(deviceDto);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(deviceRepository, never()).save(any(Device.class));
	}

	@Test
	public void update_shouldReturnNotFound_whenSensorsDoesNotExist() {
		// Arrange
		DeviceDto deviceDto = testDeviceDto1;

		Mockito.lenient().when(sensorRepository.checkSensorsExists(deviceDto.getSensors())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(deviceDto);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(deviceRepository, never()).save(any(Device.class));
	}

	@Test
	public void update_shouldReturnNotFound_whenLocationDoesNotExist() {
		// Arrange
		DeviceDto deviceDto = testDeviceDto1;

		Mockito.lenient().when(locationRepository.exists(deviceDto.getLongitude(), deviceDto.getLatitude()))
				.thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(deviceDto);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(deviceRepository, never()).save(any(Device.class));
	}

	@Test
	public void delete_shouldDeleteDeviceAndReturnOk_whenDeviceExists() {
		// Arrange
		int id = 1;

		when(deviceRepository.existsById(id)).thenReturn(true);

		// Act
		ResponseEntity<HttpStatus> response = underTest.delete(id);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(deviceRepository, times(1)).deleteById(id);
	}

	@Test
	public void delete_shouldReturnNotFound_whenDeviceDoesNotExist() {
		// Arrange
		int id = 1;

		when(deviceRepository.existsById(id)).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.delete(id);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(deviceRepository, never()).deleteById(id);
	}

}

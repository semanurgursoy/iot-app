package com.iot.data_management_system.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.iot.data_management_system.Business.concretes.SignalManager;
import com.iot.data_management_system.dataAccess.SignalRepository;
import com.iot.data_management_system.dataAccess.DeviceRepository;
import com.iot.data_management_system.entities.Device;
import com.iot.data_management_system.entities.Signal;
import com.iot.data_management_system.entities.device.Brand;
import com.iot.data_management_system.entities.device.Location;
import com.iot.data_management_system.entities.device.Mode;
import com.iot.data_management_system.entities.device.Sensor;
import com.iot.data_management_system.entities.dto.SignalDto;

@ExtendWith(MockitoExtension.class)
public class SignaServiceTest {

	@Mock
	private SignalRepository signalRepository;

	@Mock
	private DeviceRepository deviceRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private SignalManager underTest;

	Sensor testSensor1 = new Sensor(1, "Sıcaklık Sensörü", "TH-100", "abc", "Kablosuz", "Ortam sıcaklığını ölçmek",
			"100g", "%100", "1 yıl", "-20°C - 50°C", "1 saniye", true, true, true, false, true, new Mode(2, "normal"),
			new Brand(1, "ACME"), new ArrayList<>());

	Sensor testSensor2 = new Sensor(2, "Nem Sensörü", "HS-200", "def", "Kablolu", "Ortam nemini ölçmek", "50g", "%70",
			"2 yıl", "%10 - %90", "2 saniye", true, true, false, false, false, new Mode(1, "airplane"),
			new Brand(1, "ACME"), new ArrayList<>());

	Device testDevice1 = new Device(1, "Cihaz 2", "Model Y", "SN654321", "Genel Kullanım", "2 Kg", "Lityum", "1 Yıl",
			null, new Brand(1, "ACME"), new Mode(1, "airplane"), Arrays.asList(new Sensor(), new Sensor()),
			new Location());

	Device testDevice2 = new Device(2, "Cihaz 3", "Model Z", "SN987654", "Hava Kalitesi Kontrolü", "3.5 Kg",
			"Lityum İyon", "2 Yıl", LocalDate.of(2023, 10, 15), new Brand(2, "xiaomi"), new Mode(2, "normal"),
			new ArrayList<Sensor>(), new Location());

	Signal testSignal1 = new Signal(1, testDevice1, LocalDateTime.now(), 25.3, 50.2, 2.1, 750.0);
	Signal testSignal2 = new Signal(2, testDevice1, LocalDateTime.now().plusMinutes(10), 22.8, 62.1, 1.5, 500.0);
	SignalDto testSignalDto1 = new SignalDto("SN654321", LocalDateTime.now(), 25.3, 50.2, 2.1, 750.0);
	SignalDto testSignalDto2 = new SignalDto("SN987654", LocalDateTime.now().plusMinutes(10), 22.8, 62.1, 1.5, 500.0);

	@Test
	public void getAll_shouldReturnAllSignalsAsDtos_whenSignalsExist() {
		// Arrange
		List<Signal> signalList = List.of(testSignal1, testSignal2);
		List<SignalDto> expectedDtoList = List.of(testSignalDto1, testSignalDto2);

		when(signalRepository.findAll()).thenReturn(signalList);
		for (int i = 0; i < signalList.size(); i++)
			when(modelMapper.map(signalList.get(i), SignalDto.class)).thenReturn(expectedDtoList.get(i));

		// Act
		ResponseEntity<List<SignalDto>> response = underTest.getAll();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDtoList, response.getBody());
		verify(signalRepository).findAll();
		verify(modelMapper, times(2)).map(any(Signal.class), eq(SignalDto.class));
	}

	@Test
	public void getAllByDevice_shouldReturnSignalsForDevice_whenDeviceExists() {
		// Arrange
		String serialNumber = "SN654321";
		Device device = testDevice1;
		List<Signal> signalList = List.of(testSignal1, testSignal2);
		List<SignalDto> expectedDtoList = List.of(testSignalDto1, testSignalDto2);

		when(deviceRepository.existsBySerialNumber(serialNumber)).thenReturn(true);
		when(deviceRepository.findBySerialNumber(serialNumber)).thenReturn(device);
		when(signalRepository.findByDeviceId(device.getId())).thenReturn(signalList);
		for (int i = 0; i < signalList.size(); i++)
			when(modelMapper.map(signalList.get(i), SignalDto.class)).thenReturn(expectedDtoList.get(i));

		// Act
		ResponseEntity<List<SignalDto>> response = underTest.getAllByDevice(serialNumber);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDtoList, response.getBody());
	}

	@Test
	public void getAllByDevice_shouldReturnNotFound_whenDeviceDoesNotExist() {
		// Arrange
		String serialNumber = "unknownnumber";

		when(deviceRepository.existsBySerialNumber(serialNumber)).thenReturn(false);

		// Act
		ResponseEntity<List<SignalDto>> response = underTest.getAllByDevice(serialNumber);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void getAllByDate_shouldReturnSignalsForDate_whenDateExists() {
		// Arrange
		LocalDate date = LocalDate.of(2024, 3, 4);
		List<Signal> signalList = List.of(testSignal1, testSignal2);
		List<SignalDto> expectedDtoList = List.of(testSignalDto1, testSignalDto2);

		when(signalRepository.existsByLocalDate(date)).thenReturn(true);
		when(signalRepository.findByDate(date)).thenReturn(signalList);
		for (int i = 0; i < signalList.size(); i++)
			when(modelMapper.map(signalList.get(i), SignalDto.class)).thenReturn(expectedDtoList.get(i));

		// Act
		ResponseEntity<List<SignalDto>> response = underTest.getAllByDate(date);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDtoList, response.getBody());
	}

	@Test
	public void getAllByDate_shouldReturnNotFound_whenDateDoesNotExist() {
		// Arrange
		LocalDate date = LocalDate.of(2023, 12, 31);

		when(signalRepository.existsByLocalDate(date)).thenReturn(false);

		// Act
		ResponseEntity<List<SignalDto>> response = underTest.getAllByDate(date);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void getAllByTemperature_shouldReturnSignalsForTemperatureAndCondition_whenValid() {
		// Arrange
		double temperature = 25.5;
		int condition = 1;
		List<Signal> signalList = List.of(testSignal1, testSignal2);
		List<SignalDto> expectedDtoList = List.of(testSignalDto1, testSignalDto2);

		when(signalRepository.findByTemperature(temperature, condition)).thenReturn(signalList);
		for (int i = 0; i < signalList.size(); i++)
			when(modelMapper.map(signalList.get(i), SignalDto.class)).thenReturn(expectedDtoList.get(i));

		// Act
		ResponseEntity<List<SignalDto>> response = underTest.getAllByTemperature(temperature, condition);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDtoList, response.getBody());
	}

	@Test
	public void getAllByTemperature_shouldReturnBadRequest_whenConditionInvalid() {
		// Arrange
		double temperature = 30.0;
		int condition = 4;

		// Act
		ResponseEntity<List<SignalDto>> response = underTest.getAllByTemperature(temperature, condition);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(signalRepository, times(0)).findByTemperature(anyDouble(), anyInt());
	}

	@Test
	public void getAllByHumidity_shouldReturnSignalsForHumidityAndCondition_whenValid() {
		// Arrange
		double humidity = 65.0;
		int condition = 1;
		List<Signal> signalList = List.of(testSignal1, testSignal2);
		List<SignalDto> expectedDtoList = List.of(testSignalDto1, testSignalDto2);

		when(signalRepository.findByHumidity(humidity, condition)).thenReturn(signalList);
		for (int i = 0; i < signalList.size(); i++)
			when(modelMapper.map(signalList.get(i), SignalDto.class)).thenReturn(expectedDtoList.get(i));

		// Act
		ResponseEntity<List<SignalDto>> response = underTest.getAllByHumidity(humidity, condition);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDtoList, response.getBody());
	}

	@Test
	public void getAllByHumidity_shouldReturnBadRequest_whenConditionInvalid() {
		// Arrange
		double humidity = 70.0;
		int condition = 4;

		// Act
		ResponseEntity<List<SignalDto>> response = underTest.getAllByHumidity(humidity, condition);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(signalRepository, times(0)).findByHumidity(anyDouble(), anyInt());
	}

	@Test
	public void getAllByWindSpeed_shouldReturnSignalsForWindSpeedAndCondition_whenValid() {
		// Arrange
		double windSpeed = 15.0;
		int condition = 1;
		List<Signal> signalList = List.of(testSignal1, testSignal2);
		List<SignalDto> expectedDtoList = List.of(testSignalDto1, testSignalDto2);

		when(signalRepository.findByWindSpeed(windSpeed, condition)).thenReturn(signalList);
		for (int i = 0; i < signalList.size(); i++)
			when(modelMapper.map(signalList.get(i), SignalDto.class)).thenReturn(expectedDtoList.get(i));

		// Act
		ResponseEntity<List<SignalDto>> response = underTest.getAllByWindSpeed(windSpeed, condition);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDtoList, response.getBody());
	}

	@Test
	public void getAllByWindSpeed_shouldReturnBadRequest_whenConditionInvalid() {
		// Arrange
		double windSpeed = 20.0;
		int condition = 4;

		// Act
		ResponseEntity<List<SignalDto>> response = underTest.getAllByWindSpeed(windSpeed, condition);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(signalRepository, times(0)).findByWindSpeed(anyDouble(), anyInt());
	}

	@Test
	public void getAllByLightIntensity_shouldReturnSignalsForLightIntensityAndCondition_whenValid() {
		// Arrange
		double lightIntensity = 500.0;
		int condition = 1;
		List<Signal> signalList = List.of(testSignal1, testSignal2);
		List<SignalDto> expectedDtoList = List.of(testSignalDto1, testSignalDto2);

		when(signalRepository.findByLightIntensity(lightIntensity, condition)).thenReturn(signalList);
		for (int i = 0; i < signalList.size(); i++)
			when(modelMapper.map(signalList.get(i), SignalDto.class)).thenReturn(expectedDtoList.get(i));

		// Act
		ResponseEntity<List<SignalDto>> response = underTest.getAllByLightIntensity(lightIntensity, condition);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDtoList, response.getBody());
	}

	@Test
	public void getAllByLightIntensity_shouldReturnBadRequest_whenConditionInvalid() {
		// Arrange
		double lightIntensity = 700.0;
		int condition = 4;

		// Act
		ResponseEntity<List<SignalDto>> response = underTest.getAllByLightIntensity(lightIntensity, condition);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(signalRepository, times(0)).findByLightIntensity(anyDouble(), anyInt());
	}

	@Test
	public void add_shouldAddSignalAndReturnOk_whenDeviceExists() {
		// Arrange
		SignalDto dto = testSignalDto1;
		Device device = testDevice1;

		when(deviceRepository.existsBySerialNumber(dto.getDevice())).thenReturn(true);
		when(deviceRepository.findBySerialNumber(dto.getDevice())).thenReturn(device);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(dto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(signalRepository).save(any(Signal.class));
	}

	@Test
	public void add_shouldReturnBadRequest_whenDeviceDoesNotExist() {
		// Arrange
		SignalDto dto = testSignalDto1;

		when(deviceRepository.existsBySerialNumber(dto.getDevice())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(dto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(signalRepository, times(0)).save(any(Signal.class));
	}

	@Test
	public void addList_shouldAddAllSignalsAndReturnOk_whenAllDevicesExist() {
		// Arrange
		List<SignalDto> dtoList = List.of(testSignalDto1, testSignalDto2);
		Device device1 = testDevice1;
		Device device2 = testDevice2;

		when(deviceRepository.existsBySerialNumber(dtoList.get(0).getDevice())).thenReturn(true);
		when(deviceRepository.findBySerialNumber(dtoList.get(0).getDevice())).thenReturn(device1);
		when(deviceRepository.existsBySerialNumber(dtoList.get(1).getDevice())).thenReturn(true);
		when(deviceRepository.findBySerialNumber(dtoList.get(1).getDevice())).thenReturn(device2);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(dtoList);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(signalRepository, times(2)).save(any(Signal.class));
	}

	@Test
	public void addList_shouldReturnBadRequest_whenOneDeviceDoesNotExist() {
		// Arrange
		List<SignalDto> dtoList = List.of(testSignalDto1, testSignalDto2);

		for(SignalDto dto: dtoList)
			Mockito.lenient().when(deviceRepository.existsBySerialNumber(dto.getDevice())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(dtoList);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(signalRepository, times(0)).save(any(Signal.class));
	}

}

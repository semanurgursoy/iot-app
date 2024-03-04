package com.iot.data_management_system.services.device;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
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

import com.iot.data_management_system.Business.concretes.device.ModeManager;
import com.iot.data_management_system.dataAccess.device.ModeRepository;
import com.iot.data_management_system.entities.device.Mode;
import com.iot.data_management_system.entities.dto.ModeDto;

@ExtendWith(MockitoExtension.class)
public class ModeServiceTest {
	@Mock
	private ModeRepository modeRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private ModeManager underTest;

	Mode testMode1 = new Mode(1, "airplane");
	Mode testMode2 = new Mode(2, "normal");
	ModeDto testModeDto1 = new ModeDto("airplane");
	ModeDto testModeDto2 = new ModeDto("normal");

	@Test
	void getAll_shouldReturnOkAndListOfModeDtosWhenModesExist() {
		// Arrange
		List<Mode> mockModeList = Arrays.asList(testMode1, testMode2);
		List<ModeDto> expectedDtoList = Arrays.asList(testModeDto1, testModeDto1);

		when(modeRepository.findAll()).thenReturn(mockModeList);
		for (int i = 0; i < mockModeList.size(); i++)
			when(modelMapper.map(mockModeList.get(i), ModeDto.class)).thenReturn(expectedDtoList.get(i));

		// Act
		ResponseEntity<List<ModeDto>> response = underTest.getAll();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDtoList, response.getBody());
	}

	@Test
	void getByName_shouldReturnOkAndModeDtoWhenModeExists() {
		// Arrange
		String existingMode = "Standard";
		Mode mockMode = testMode1;
		;
		ModeDto expectedDto = testModeDto1;

		when(modeRepository.existsByMode(existingMode)).thenReturn(true);
		when(modeRepository.findByMode(existingMode)).thenReturn(mockMode);
		when(modelMapper.map(any(Mode.class), eq(ModeDto.class))).thenReturn(expectedDto);

		// Act
		ResponseEntity<ModeDto> response = underTest.getByName(existingMode);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedDto, response.getBody());
	}

	@Test
	void getByName_shouldReturnNotFoundWhenModeDoesNotExist() {
		// Arrange
		String nonExistentMode = "UnknownMode";

		when(modeRepository.existsByMode(nonExistentMode)).thenReturn(false);

		// Act
		ResponseEntity<ModeDto> response = underTest.getByName(nonExistentMode);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	void add_shouldReturnOkWhenModeAddedSuccessfully() {
		// Arrange
		ModeDto validDto = testModeDto1;

		when(modeRepository.existsByMode(validDto.getMode())).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(validDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(modeRepository).save(any(Mode.class));
	}

	@Test
	void add_shouldReturnBadRequestWhenModeAlreadyExists() {
		// Arrange
		ModeDto duplicateModeDto = testModeDto1;

		when(modeRepository.existsByMode(duplicateModeDto.getMode())).thenReturn(true);

		// Act
		ResponseEntity<HttpStatus> response = underTest.add(duplicateModeDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(modeRepository, never()).save(any(Mode.class));
	}

	@Test
	void update_shouldReturnOkWhenModeUpdatedSuccessfully() {
		// Arrange
		String existingMode = "airplane";
		String newMode = "sleep";
		Mode mockMode = testMode1;

		when(modeRepository.existsByMode(existingMode)).thenReturn(true);
		when(modeRepository.findByMode(existingMode)).thenReturn(mockMode);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(existingMode, newMode);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(newMode, mockMode.getMode());
		verify(modeRepository).save(mockMode);
	}

	@Test
	void update_shouldReturnNotFoundWhenModeDoesNotExist() {
		// Arrange
		String nonExistentMode = "UnknownMode";
		String newMode = "NewMode";

		when(modeRepository.existsByMode(nonExistentMode)).thenReturn(false);

		// Act
		ResponseEntity<HttpStatus> response = underTest.update(nonExistentMode, newMode);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(modeRepository, never()).save(any(Mode.class));
	}
	
	@Test
    void delete_shouldReturnOkWhenModeIsDeleted() {
        // Arrange
        int existingId = 1;

        when(modeRepository.existsById(existingId)).thenReturn(true);

        // Act
        ResponseEntity<HttpStatus> response = underTest.delete(existingId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(modeRepository).deleteById(existingId); 
    }

    @Test
    void delete_shouldReturnNotFoundWhenModeDoesNotExist() {
        // Arrange
        int nonExistentId = 10;

        when(modeRepository.existsById(nonExistentId)).thenReturn(false);

        // Act
        ResponseEntity<HttpStatus> response = underTest.delete(nonExistentId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(modeRepository, never()).deleteById(anyInt());
    }

}

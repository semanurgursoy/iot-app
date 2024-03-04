package com.iot.data_management_system.Business.concretes.device;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iot.data_management_system.Business.abstracts.device.ModeService;
import com.iot.data_management_system.dataAccess.device.ModeRepository;
import com.iot.data_management_system.entities.device.Mode;
import com.iot.data_management_system.entities.dto.ModeDto;

@Service
public class ModeManager implements ModeService {

	private ModeRepository modeRepository;
	private ModelMapper modelMapper;
	
	public ModeManager(ModeRepository modeRepository, ModelMapper modelMapper) {
		this.modeRepository = modeRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public ResponseEntity<List<ModeDto>> getAll() {
		List<Mode> modeList = modeRepository.findAll();
		List<ModeDto> dtoList = new ArrayList<ModeDto>();
		for(Mode mode: modeList)
			dtoList.add(modelMapper.map(mode, ModeDto.class));
		return new ResponseEntity<>(dtoList, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ModeDto> getByName(String mode) {
		if(modeRepository.existsByMode(mode)) {
			Mode modeObject = modeRepository.findByMode(mode);
			ModeDto dto = modelMapper.map(modeObject, ModeDto.class);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<HttpStatus> add(ModeDto modeDto) {
		if(!modeRepository.existsByMode(modeDto.getMode())) {
			Mode mode = new Mode();
			mode.setMode(modeDto.getMode());
			modeRepository.save(mode);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<HttpStatus> update(String mode, String newMode) {
		if(modeRepository.existsByMode(mode)) {
			Mode m = modeRepository.findByMode(mode);
			m.setMode(newMode);
			modeRepository.save(m);
			
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<HttpStatus> delete(int id) {
		if(modeRepository.existsById(id)) {
			modeRepository.deleteById(id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}
}

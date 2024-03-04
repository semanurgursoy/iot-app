package com.iot.data_management_system.Business.concretes.device;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iot.data_management_system.Business.abstracts.device.BrandService;
import com.iot.data_management_system.dataAccess.device.BrandRepository;
import com.iot.data_management_system.entities.device.Brand;
import com.iot.data_management_system.entities.dto.BrandDto;

@Service
public class BrandManager implements BrandService {

	private BrandRepository brandRepository;
	private ModelMapper modelMapper;
	
	public BrandManager(BrandRepository brandRepository, ModelMapper modelMapper) {
		this.brandRepository = brandRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public ResponseEntity<List<BrandDto>> getAll() {
		List<Brand> brandList = brandRepository.findAll();
		List<BrandDto> dtoList = new ArrayList<BrandDto>();
		for(Brand brand: brandList)
			dtoList.add(modelMapper.map(brand, BrandDto.class));
		return new ResponseEntity<>(dtoList, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BrandDto> getByName(String brand) {
		if(brandRepository.existsByBrand(brand)) {
			Brand brandObjet = brandRepository.findByBrand(brand);
			BrandDto dto = modelMapper.map(brandObjet, BrandDto.class);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<HttpStatus> add(BrandDto brandDto) {
		if(!brandRepository.existsByBrand(brandDto.getBrand())) {
			Brand brand = new Brand();
			brand.setBrand(brandDto.getBrand());
			brandRepository.save(brand);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<HttpStatus> update(String brand, String newBrand) {
		if(brandRepository.existsByBrand(brand)) {
			Brand b = brandRepository.findByBrand(brand);
			b.setBrand(newBrand);
			brandRepository.save(b);
			
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<HttpStatus> delete(int id) {
		if(brandRepository.existsById(id)) {
			brandRepository.deleteById(id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

}

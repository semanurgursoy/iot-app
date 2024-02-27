package com.iot.data_management_system.WebAPIs.device;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.data_management_system.Business.abstracts.device.BrandService;
import com.iot.data_management_system.entities.device.Brand;
import com.iot.data_management_system.entities.dto.BrandDto;

@RestController
@RequestMapping("/api/brand")
public class BrandController {
	
	private BrandService brandService;

	public BrandController(BrandService brandService) {
		this.brandService = brandService;
	}
	
	@GetMapping("/getall")
	public List<BrandDto> getAll(){
		return brandService.getAll();
	}
	
	@GetMapping("/get_by_brand")
	public BrandDto getByBrand(String brand){
		return brandService.getByName(brand);
	}
	
	@PostMapping("/add")
	public ResponseEntity<HttpStatus> add(@RequestBody Brand brand){
		return brandService.add(brand);
	}
	
	@PostMapping("/update")
	public ResponseEntity<HttpStatus> update(String brand, String newBrand){
		return brandService.update(brand, newBrand);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<HttpStatus> delete(int id){
		return brandService.delete(id);
	}
}

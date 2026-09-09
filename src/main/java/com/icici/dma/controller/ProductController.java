package com.icici.dma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dma.dto.ProductDto;
import com.icici.dma.service.ProductService;

@RestController
@RequestMapping("/DMAPayoutWeb3")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@GetMapping("/products")
	public ResponseEntity<List<String>> getProducts() {

	    return ResponseEntity.ok(productService.getProducts());
	}
	
	@GetMapping("/subProducts")
	public ResponseEntity<List<String>> getSubProducts(
	        @RequestParam String product) {

	    return ResponseEntity.ok(
	            productService.getSubProducts(product));
	}
	
	@GetMapping("/masters")
	public ResponseEntity<List<String>> getMasters(
	        @RequestParam String product,
	        @RequestParam String subProduct) {

	    return ResponseEntity.ok(
	            productService.getMasters(product, subProduct));
	}
	
	@PostMapping("/createProduct")
	public ResponseEntity<String> createProduct(@RequestBody ProductDto dto, @RequestHeader("user") String user) {
		productService.createProduct(dto, user);
		return ResponseEntity.ok("Created Successfully");
	}
}
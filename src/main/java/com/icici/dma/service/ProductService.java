package com.icici.dma.service;

import java.util.List;

import com.icici.dma.dto.ProductDto;

public interface ProductService {

	 List<String> getProducts();

	 List<String> getSubProducts(String product);

	 List<String> getMasters(String product, String subProduct);
	 
	 void createProduct(ProductDto dto, String username);


}
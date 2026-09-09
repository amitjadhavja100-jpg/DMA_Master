package com.icici.dma.serviceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.repository.ProductMasterRepository;
import com.icici.dma.service.ProductMasterService;

@Service
public class ProductMasterserviceImpl implements ProductMasterService {

	@Autowired
	public ProductMasterRepository ProductMasterRepo;

	@Override
	public List<String> getAllProductMatser() {

		List<String> productMasterNames = ProductMasterRepo.FindAllProductNames().filter(list -> !list.isEmpty())
				.orElseThrow(() -> new ResourceNotFoundException("Product Master Names Not Found"));

		List<String> masterList = productMasterNames.stream().map(s -> s.replace("_", " ").trim())
				.map(s -> Arrays.stream(s.split("\\s+")).map(word -> {
					if (word.equalsIgnoreCase("gst")) {
						return "GST"; // special case
					}
					return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
				}).collect(Collectors.joining(" "))).collect(Collectors.toList());

		return masterList;
	}

}

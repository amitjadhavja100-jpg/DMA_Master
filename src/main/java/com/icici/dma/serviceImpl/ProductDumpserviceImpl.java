package com.icici.dma.serviceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icici.dma.controller.ReportController;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.repository.ProductDumpRepository;
import com.icici.dma.service.ProductDumpService;

@Service
public class ProductDumpserviceImpl implements ProductDumpService {

	private static final Logger logger = LogManager.getLogger(ReportController.class);

	@Autowired
	public ProductDumpRepository ProductDumpRepo;

	@Override
	public List<String> getAllProductDump() {

		logger.info("Product Dump service called :");

//		List<String> productDumpNames = ProductDumpRepo.FindAllProductNames().filter(list -> !list.isEmpty())
//				.orElseThrow(() -> new ResourceNotFoundException("Product Dump Names Not Found"));

		List<String> productDumpNames = ProductDumpRepo.FindAllDumpName();

		if (productDumpNames == null || productDumpNames.isEmpty()) {
			throw new ResourceNotFoundException("Product Dump Names Not Found");
		}

		logger.info("Product Dump row Data : {}", productDumpNames);

//		List<String> masterList = productDumpNames.stream()
//				.map(s -> Arrays.stream(s.toLowerCase().replace("_", " ").trim().split("\\s+"))
//						.filter(w -> !w.isEmpty()).map(w -> Character.toUpperCase(w.charAt(0)) + w.substring(1))
//						.collect(Collectors.joining(" ")))
//				.collect(Collectors.toList());

//		logger.info("Product Dump filter Data : {}",productDumpNames);
		return productDumpNames;
	}

}

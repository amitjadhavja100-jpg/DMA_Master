/*package com.icici.dma.controller;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.service.CreditCardBillingService;

@CrossOrigin("*")
@RestController
@RequestMapping("/creditCardBilling")
public class CreditCardBillingController {

	private static final Logger logger = LogManager.getLogger(CreditCardBillingController.class);

	@Autowired
	public CreditCardBillingService creditCarddBillingService;

	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("product") String product, @RequestParam("master") String masterType) {

		File savedFile = null;

		try {

			// ---------------------------------------------------------
			// Step 1 : Validate if file is empty
			// ---------------------------------------------------------
			if (file.isEmpty()) {

				logger.warn("File upload request received but file is empty.");

				return ResponseEntity.badRequest().body("Uploaded file is empty");
			}

			// ---------------------------------------------------------
			// Step 2 : Define upload directory where file will be stored
			// ---------------------------------------------------------
			
			String BASE_PATH = "D:/app/dma/CreditCard/";
			
			createDirectories();

			// file upload
			 savedFile = creditCarddBillingService.saveFileOnServer(file, BASE_PATH);

			// ---------------------------------------------------------
			// Step 5 : Execute SQL Loader to load file data into database
			// ---------------------------------------------------------
			logger.info("Starting SQL Loader execution for file: {}", savedFile.getAbsolutePath());

			creditCarddBillingService.executeSqlLoader(savedFile.getAbsolutePath());

			logger.info("SQL Loader completed successfully for file: {}", savedFile.getName());

			// ---------------------------------------------------------
			// Step 6 : Delete file after successful processing
			// ---------------------------------------------------------
//	        boolean deleted = savedFile.delete();
//	 
//	        if (deleted) {
//	 
//	            logger.info("Temporary file deleted successfully from server: {}", savedFile.getAbsolutePath());
//	 
//	        } else {
//	 
//	            logger.warn("File processed but deletion failed. File path: {}", savedFile.getAbsolutePath());
//	        }
//	 
			// ---------------------------------------------------------
			// Step 7 : Send success response to UI
			// ---------------------------------------------------------
			return ResponseEntity.ok("File processed successfully");

		} catch (Exception e) {

			// ---------------------------------------------------------
			// Error Handling : Log error and attempt file cleanup
			// ---------------------------------------------------------
			logger.error("Exception occurred during file upload or SQL Loader processing", e);

			// Attempt to delete file if exception occurred after upload
//	        if (savedFile != null && savedFile.exists()) {
//	 
//	            boolean deleted = savedFile.delete();
//	 
//	            if (deleted) {
//	 
//	                logger.info("Temporary file deleted after failure: {}", savedFile.getAbsolutePath());
//	 
//	            } else {
//	 
//	                logger.warn("Failed to delete temporary file after processing failure: {}", savedFile.getAbsolutePath());
//	            }
//	        }

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("File processing failed due to internal error");
		}

	}

	private void createDirectories() {

		new File("/app/dma/CreditCard/ctl").mkdirs();
		new File("/app/dma/CreditCard/log").mkdirs();
		new File("/app/dma/CreditCard/bad").mkdirs();

	}
}
*/
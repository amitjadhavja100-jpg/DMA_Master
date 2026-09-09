package com.icici.dma.globalException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<String> handlerNullPointerException(NullPointerException ex) {

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handlerResourceNotFoundException(ResourceNotFoundException ex) {
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<String> handlerDataBaseException(DataAccessException ex) {
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Database error while fetching Data");
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handlerIllegalArgumentException(IllegalArgumentException ex) {
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handlerRuntimeException(RuntimeException ex) {
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handlerException(Exception ex) {
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");

	}

}

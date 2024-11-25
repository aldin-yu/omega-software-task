package com.example.omega.exceptions;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationExceptions (MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("status", String.valueOf(ex.getStatusCode().value()));
		errorResponse.put("error", ex.getReason());
		return new ResponseEntity<>(errorResponse, ex.getStatusCode());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleMalformedJson(HttpMessageNotReadableException ex) {
		Map<String, String> response = new HashMap<>();
		response.put("error", "Malformed JSON request");
		response.put("message", "The JSON request body is invalid or missing required fields.");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UnrecognizedPropertyException.class)
	public ResponseEntity<?> handleUnrecognizedField(UnrecognizedPropertyException ex) {
		Map<String, String> response = new HashMap<>();
		response.put("error", "Unrecognized field");
		response.put("message", "Field '" + ex.getPropertyName() + "' is not allowed.");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}


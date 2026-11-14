package org.springframework.samples.petclinic.visits.infrastructure.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.visits.domain.model.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice(basePackages = "org.springframework.samples.petclinic.visits.infrastructure.web.controller")
public class VisitExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(ResourceNotFoundException ex) {
		log.warn("Resource not found: {}", ex.getMessage());
		var error = new ErrorResponse(
			HttpStatus.NOT_FOUND.value(),
			"Not Found",
			ex.getMessage(),
			LocalDateTime.now()
		);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
		log.error("Unexpected error occurred: ", ex);
		var error = new ErrorResponse(
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			"Internal Server Error",
			"An unexpected error occurred processing your request.",
			LocalDateTime.now()
		);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	public record ErrorResponse(
		int status,
		String error,
		String message,
		LocalDateTime timestamp
	) {}
}

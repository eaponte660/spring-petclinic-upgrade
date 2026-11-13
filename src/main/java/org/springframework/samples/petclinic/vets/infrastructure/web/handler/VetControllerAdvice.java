package org.springframework.samples.petclinic.vets.infrastructure.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.vets.domain.exception.VetDomainValidationException;
import org.springframework.samples.petclinic.vets.domain.exception.VetNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(basePackages = "org.springframework.samples.petclinic.vets.web")
public class VetControllerAdvice {

	@ExceptionHandler({
		IllegalArgumentException.class,
		IllegalStateException.class,
		VetDomainValidationException.class
	})
	public ResponseEntity<ErrorResponse> handleDomainValidationException(RuntimeException ex) {
		log.warn("Domain validation error: {}", ex.getMessage());
		var error = new ErrorResponse(
			HttpStatus.BAD_REQUEST.value(),
			"Bad Request",
			ex.getMessage(),
			LocalDateTime.now()
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
			.map(error -> error.getField() + ": " + error.getDefaultMessage())
			.collect(Collectors.joining(", "));

		log.warn("Validation failed: {}", message);
		var error = new ErrorResponse(
			HttpStatus.BAD_REQUEST.value(),
			"Validation Failed",
			message,
			LocalDateTime.now()
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(VetNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException ex) {
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

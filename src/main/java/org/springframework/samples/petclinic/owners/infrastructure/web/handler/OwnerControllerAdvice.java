package org.springframework.samples.petclinic.owners.infrastructure.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.owners.domain.exception.DomainValidationException;
import org.springframework.samples.petclinic.owners.domain.exception.DuplicatePetNameException;
import org.springframework.samples.petclinic.owners.domain.exception.OwnerNotFoundException;
import org.springframework.samples.petclinic.owners.domain.exception.PetNotFoundException;
import org.springframework.samples.petclinic.owners.domain.exception.PetTypeNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice(basePackages = "org.springframework.samples.petclinic.owners.web")
public class OwnerControllerAdvice {

	@ExceptionHandler({DomainValidationException.class, DuplicatePetNameException.class})
	public ResponseEntity<ErrorResponse> handleDomainValidationException(Exception ex) {
		log.warn("Domain validation error caught: '{}'. Returning HTTP 400.", ex.getMessage());

		var error = new ErrorResponse(
			HttpStatus.BAD_REQUEST.value(),
			ex.getMessage(),
			LocalDateTime.now()
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler({OwnerNotFoundException.class, PetTypeNotFoundException.class, PetNotFoundException.class})
	public ResponseEntity<ErrorResponse> handleNotFoundException(Exception ex) {
		log.warn("Resource not found error caught: '{}'. Returning HTTP 404.", ex.getMessage());

		var error = new ErrorResponse(
			HttpStatus.NOT_FOUND.value(),
			ex.getMessage(),
			LocalDateTime.now()
		);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
		log.error("Unexpected internal server error occurred: '{}'", ex.getMessage(), ex);

		var error = new ErrorResponse(
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			"An unexpected internal server error occurred",
			LocalDateTime.now()
		);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	public record ErrorResponse(
		int status,
		String message,
		LocalDateTime timestamp
	) {}
}

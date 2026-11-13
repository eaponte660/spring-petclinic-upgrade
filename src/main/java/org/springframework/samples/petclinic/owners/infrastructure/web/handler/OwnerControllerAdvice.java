package org.springframework.samples.petclinic.owners.infrastructure.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.owners.domain.exception.DomainValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "org.springframework.samples.petclinic.owners.web")
public class OwnerControllerAdvice {

	@ExceptionHandler(DomainValidationException.class)
	public ResponseEntity<String> handleDomainValidationException(DomainValidationException ex) {
		log.warn("Intento de operación con datos de dominio inválidos: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
}

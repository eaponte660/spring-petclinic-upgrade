package org.springframework.samples.petclinic.owners.domain.exception;

public class DomainValidationException extends RuntimeException {

	public DomainValidationException(String message) {
		super(message);
	}
}

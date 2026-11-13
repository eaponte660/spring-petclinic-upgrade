package org.springframework.samples.petclinic.vets.domain.exception;

public class VetDomainValidationException extends RuntimeException {

	public VetDomainValidationException(String message) {
		super(message);
	}
}

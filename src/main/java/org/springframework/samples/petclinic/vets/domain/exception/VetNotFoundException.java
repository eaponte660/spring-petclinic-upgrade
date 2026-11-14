package org.springframework.samples.petclinic.vets.domain.exception;

public class VetNotFoundException extends VetDomainValidationException {

	public VetNotFoundException(String message) {
		super(message);
	}
}

package org.springframework.samples.petclinic.owners.domain.exception;

public class PetNotFoundException extends DomainValidationException {

	public PetNotFoundException(String message) {
		super(message);
	}
}

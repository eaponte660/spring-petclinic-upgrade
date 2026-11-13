package org.springframework.samples.petclinic.owners.domain.exception;

public class PetTypeNotFoundException extends DomainValidationException {

	public PetTypeNotFoundException(String message) {
		super(message);
	}
}

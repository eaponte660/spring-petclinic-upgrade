package org.springframework.samples.petclinic.owners.domain.exception;

public class DuplicatePetNameException extends DomainValidationException {

	public DuplicatePetNameException(String message) {
		super(message);
	}
}

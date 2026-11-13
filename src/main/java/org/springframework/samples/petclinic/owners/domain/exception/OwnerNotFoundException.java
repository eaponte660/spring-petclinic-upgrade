package org.springframework.samples.petclinic.owners.domain.exception;

public class OwnerNotFoundException extends DomainValidationException {

	public OwnerNotFoundException(String message) {
		super(message);
	}
}

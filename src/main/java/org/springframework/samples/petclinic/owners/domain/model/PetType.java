package org.springframework.samples.petclinic.owners.domain.model;

import org.springframework.samples.petclinic.owners.domain.exception.DomainValidationException;

import java.util.Objects;

public record PetType(Long id, String name) {

	public PetType {
		Objects.requireNonNull(name, "Pet type name must not be null");
		if (name.isBlank()) {
			throw new DomainValidationException("Pet type name must not be blank");
		}
	}
}

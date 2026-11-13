package org.springframework.samples.petclinic.owners.domain.model;

import org.springframework.samples.petclinic.owners.domain.exception.DomainValidationException;

public final class DomainGuard {

	private DomainGuard() {}

	public static String notBlankAndMax(String value, String fieldName, int maxLength) {
		notBlank(value, fieldName);
		if (value.length() > maxLength) {
			throw new DomainValidationException(fieldName + " must not exceed " + maxLength + " characters");
		}
		return value;
	}

	public static String notBlank(String value, String fieldName) {
		if (value == null || value.isBlank()) {
			throw new DomainValidationException(fieldName + " must not be blank");
		}
		return value;
	}
}

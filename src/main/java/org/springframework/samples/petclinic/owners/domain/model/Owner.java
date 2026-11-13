package org.springframework.samples.petclinic.owners.domain.model;

import lombok.Getter;
import org.springframework.samples.petclinic.owners.domain.exception.DomainValidationException;

@Getter
public class Owner {

	private Long id;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String telephone;

	public Owner(Long id, String firstName, String lastName, String address, String city, String telephone) {
		validateAndAssign(firstName, lastName, address, city, telephone);
		this.id = id;
	}

	public void updateInfo(String firstName, String lastName, String address, String city, String telephone) {
		validateAndAssign(firstName, lastName, address, city, telephone);
	}

	private void validateAndAssign(String firstName, String lastName, String address, String city, String telephone) {
		this.firstName = validateField(firstName, "First name", 30);
		this.lastName = validateField(lastName, "Last name", 30);
		this.address = validateField(address, "Address", 255);
		this.city = validateField(city, "City", 80);
		this.telephone = validateTelephone(telephone);
	}

	private String validateField(String value, String fieldName, int maxLength) {
		if (value == null || value.isBlank()) {
			throw new DomainValidationException(fieldName + " must not be blank");
		}
		if (value.length() > maxLength) {
			throw new DomainValidationException(fieldName + " must not exceed " + maxLength + " characters");
		}
		return value;
	}

	private String validateTelephone(String telephone) {
		if (telephone == null || telephone.isBlank()) {
			throw new DomainValidationException("Telephone must not be blank");
		}

		if (!telephone.matches("\\d{10}")) {
			throw new DomainValidationException("Telephone must be exactly 10 digits");
		}
		return telephone;
	}
}

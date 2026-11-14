package org.springframework.samples.petclinic.owners.domain.model;

import lombok.Getter;
import org.springframework.samples.petclinic.owners.domain.exception.DomainValidationException;

import java.util.List;

import static org.springframework.samples.petclinic.owners.domain.model.DomainGuard.notBlank;
import static org.springframework.samples.petclinic.owners.domain.model.DomainGuard.notBlankAndMax;

@Getter
public class Owner {

	private Long id;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String telephone;
	private List<Pet> pets;

	public Owner(Long id, String firstName, String lastName, String address, String city, String telephone, List<Pet> pets) {
		validateAndAssign(firstName, lastName, address, city, telephone);
		this.id = id;
		this.pets = pets;
	}

	public void updateInfo(String firstName, String lastName, String address, String city, String telephone) {
		validateAndAssign(firstName, lastName, address, city, telephone);
	}

	private void validateAndAssign(String firstName, String lastName, String address, String city, String telephone) {
		this.firstName = notBlankAndMax(firstName, "First name", 30);
		this.lastName = notBlankAndMax(lastName, "Last name", 30);
		this.address = notBlankAndMax(address, "Address", 255);
		this.city = notBlankAndMax(city, "City", 80);
		this.telephone = validateTelephone(telephone);
	}

	private String validateTelephone(String telephone) {
		notBlank(telephone, "Phone Number");

		if (!telephone.matches("\\d{10}")) {
			throw new DomainValidationException("Telephone must be exactly 10 digits");
		}
		return telephone;
	}
}

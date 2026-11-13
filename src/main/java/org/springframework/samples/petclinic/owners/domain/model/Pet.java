package org.springframework.samples.petclinic.owners.domain.model;

import lombok.Getter;
import org.springframework.samples.petclinic.owners.domain.exception.DomainValidationException;

import java.time.LocalDate;
import java.util.Objects;

import static org.springframework.samples.petclinic.owners.domain.model.DomainGuard.notBlank;

@Getter
public class Pet {

	private Long id;
	private String name;
	private LocalDate birthDate;
	private PetType type;
	private Long ownerId;

	public Pet(Long id, String name, LocalDate birthDate, PetType type, Long ownerId) {
		validateAndAssign(name, birthDate, type, ownerId);
		this.id = id;
	}

	public void updateInfo(String name, LocalDate birthDate, PetType type) {
		validateAndAssign(name, birthDate, type, this.ownerId);
	}

	private void validateAndAssign(String name, LocalDate birthDate, PetType type, Long ownerId) {
		this.name = notBlank(name, "Name");
		this.birthDate = validateBirthDate(birthDate);
		this.type = Objects.requireNonNull(type, "Pet type must not be null");
		this.ownerId = Objects.requireNonNull(ownerId, "Owner ID must not be null");
	}

	private LocalDate validateBirthDate(LocalDate birthDate) {
		Objects.requireNonNull(birthDate, "Birth date must not be null");
		if (birthDate.isAfter(LocalDate.now())) {
			throw new DomainValidationException("Birth date cannot be in the future");
		}
		return birthDate;
	}
}

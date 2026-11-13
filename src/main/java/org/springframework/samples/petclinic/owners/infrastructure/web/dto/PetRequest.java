package org.springframework.samples.petclinic.owners.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PetRequest(
	@NotBlank(message = "Pet name cannot be blank")
	@Size(max = 30, message = "Pet name must be up to 30 characters")
	String name,

	@NotNull(message = "Birth date is required")
	LocalDate birthDate,

	@NotNull(message = "Pet type ID is required")
	Long typeId
) {}

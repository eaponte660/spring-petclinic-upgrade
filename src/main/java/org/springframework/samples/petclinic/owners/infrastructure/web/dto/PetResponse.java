package org.springframework.samples.petclinic.owners.infrastructure.web.dto;

import java.time.LocalDate;

public record PetResponse(
	Long id,
	String name,
	LocalDate birthDate,
	PetTypeResponse type,
	Long ownerId
) {}

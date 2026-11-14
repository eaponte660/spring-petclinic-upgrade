package org.springframework.samples.petclinic.owners.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PetResponse(
	Long id,
	String name,
	LocalDate birthDate,
	PetTypeResponse type,
	Long ownerId
) {}

package org.springframework.samples.petclinic.visits.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record VisitRequest(
	@NotNull(message = "Visit date is required")
	LocalDate date,

	@NotBlank(message = "Description cannot be blank")
	String description
) {}

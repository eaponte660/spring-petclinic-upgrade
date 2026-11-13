package org.springframework.samples.petclinic.visits.domain.model;

import java.time.LocalDate;

public record Visit(
	Long id,
	LocalDate date,
	String description,
	Long petId
) {}

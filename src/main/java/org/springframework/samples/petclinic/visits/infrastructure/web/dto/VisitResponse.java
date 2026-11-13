package org.springframework.samples.petclinic.visits.infrastructure.web.dto;

import java.time.LocalDate;

public record VisitResponse(
	Long id,
	LocalDate date,
	String description,
	Long petId
) {}

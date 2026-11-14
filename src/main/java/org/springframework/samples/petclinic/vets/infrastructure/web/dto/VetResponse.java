package org.springframework.samples.petclinic.vets.infrastructure.web.dto;

import java.util.List;

public record VetResponse(
	Long id,
	String firstName,
	String lastName,
	List<SpecialtyResponse> specialties
) {}

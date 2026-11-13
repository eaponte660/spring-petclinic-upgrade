package org.springframework.samples.petclinic.vets.domain.model;

import java.util.List;

public record Vet(
	Long id,
	String firstName,
	String lastName,
	List<Specialty> specialties
) {}

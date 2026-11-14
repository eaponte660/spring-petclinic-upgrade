package org.springframework.samples.petclinic.owners.infrastructure.web.dto;

import java.util.List;

public record OwnerResponse(
	Long id,
	String firstName,
	String lastName,
	String address,
	String city,
	String telephone,
	List<PetResponse> pets
) {}

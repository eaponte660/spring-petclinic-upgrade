package org.springframework.samples.petclinic.owners.infrastructure.web.dto;

public record OwnerResponse(
	Long id,
	String firstName,
	String lastName,
	String address,
	String city,
	String telephone
) {}

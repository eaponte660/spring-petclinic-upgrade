package org.springframework.samples.petclinic.owners.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OwnerRequest(
	@NotBlank(message = "First name cannot be blank")
	@Size(max = 30, message = "First name must be up to 30 characters")
	String firstName,

	@NotBlank(message = "Last name cannot be blank")
	@Size(max = 30, message = "Last name must be up to 30 characters")
	String lastName,

	@NotBlank(message = "Address cannot be blank")
	@Size(max = 255, message = "Address must be up to 255 characters")
	String address,

	@NotBlank(message = "City cannot be blank")
	@Size(max = 80, message = "City must be up to 80 characters")
	String city,

	@NotBlank(message = "Telephone cannot be blank")
	@Pattern(regexp = "\\d{10}", message = "Telephone must be exactly 10 digits")
	String telephone
) {}

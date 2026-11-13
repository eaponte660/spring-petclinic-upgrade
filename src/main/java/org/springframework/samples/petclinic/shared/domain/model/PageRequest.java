package org.springframework.samples.petclinic.shared.domain.model;

import jakarta.validation.constraints.Min;

public record PageRequest(
	@Min(value = 0, message = "Page index must be greater than or equal to 0")
	int page,

	@Min(value = 1, message = "Page size must be greater than or equal to 1")
	int size
) {}

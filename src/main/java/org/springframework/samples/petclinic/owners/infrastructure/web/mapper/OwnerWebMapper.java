package org.springframework.samples.petclinic.owners.infrastructure.web.mapper;

import org.springframework.samples.petclinic.owners.domain.model.Owner;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.OwnerResponse;
import org.springframework.stereotype.Component;

@Component
public class OwnerWebMapper {

	public OwnerResponse toResponse(Owner owner) {
		if (owner == null) return null;
		return new OwnerResponse(
			owner.getId(),
			owner.getFirstName(),
			owner.getLastName(),
			owner.getAddress(),
			owner.getCity(),
			owner.getTelephone()
		);
	}
}

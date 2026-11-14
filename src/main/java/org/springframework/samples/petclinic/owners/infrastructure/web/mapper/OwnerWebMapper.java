package org.springframework.samples.petclinic.owners.infrastructure.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.owners.domain.model.Owner;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.OwnerResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OwnerWebMapper {

	private final PetWebMapper petMapper;

	public OwnerResponse toResponse(Owner owner) {
		if (owner == null) return null;
		return new OwnerResponse(
			owner.getId(),
			owner.getFirstName(),
			owner.getLastName(),
			owner.getAddress(),
			owner.getCity(),
			owner.getTelephone(),
			owner.getPets() != null
				? owner.getPets().stream().map(petMapper::toResponse).toList()
				: List.of()
		);
	}
}

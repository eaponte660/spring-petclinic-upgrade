package org.springframework.samples.petclinic.owners.application.port.in;

import org.springframework.samples.petclinic.owners.domain.model.Owner;

public interface SearchOwnersUseCase {

	PageResult<Owner> searchOwners(SearchOwnersQuery query);

	record SearchOwnersQuery(
		String lastName,
		PageRequest pageRequest
	) {}
}

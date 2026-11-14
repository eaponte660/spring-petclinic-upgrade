package org.springframework.samples.petclinic.owners.application.port.in;

import org.springframework.samples.petclinic.owners.domain.model.Owner;

public interface FindOwnerUseCase {

	Owner findOwnerById(FindOwnerQuery query);

	record FindOwnerQuery(Long id) {}
}

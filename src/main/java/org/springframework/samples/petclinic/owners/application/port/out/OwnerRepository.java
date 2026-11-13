package org.springframework.samples.petclinic.owners.application.port.out;

import org.springframework.samples.petclinic.owners.domain.model.Owner;

public interface OwnerRepository {

	Owner save(Owner owner);
}

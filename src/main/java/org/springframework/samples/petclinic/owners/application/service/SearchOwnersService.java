package org.springframework.samples.petclinic.owners.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.petclinic.owners.application.port.in.PageRequest;
import org.springframework.samples.petclinic.owners.application.port.in.PageResult;
import org.springframework.samples.petclinic.owners.application.port.in.SearchOwnersUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.OwnerRepository;
import org.springframework.samples.petclinic.owners.domain.model.Owner;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchOwnersService implements SearchOwnersUseCase {

	private final OwnerRepository ownerRepository;

	@Override
	public PageResult<Owner> searchOwners(SearchOwnersQuery query) {
		String lastNameToSearch = query.lastName() == null ? "" : query.lastName();
		PageRequest pageRequest = query.pageRequest();

		log.info("Searching owners with lastName filter: '{}', page: {}, size: {}",
			lastNameToSearch, pageRequest.page(), pageRequest.size());

		List<Owner> owners = ownerRepository.findByLastNameContainingIgnoreCase(
			lastNameToSearch,
			pageRequest
		);

		long totalElements = ownerRepository.countByLastNameContainingIgnoreCase(lastNameToSearch);
		int totalPages = (int) Math.ceil((double) totalElements / pageRequest.size());

		log.info("Successfully retrieved {} owners out of {} total elements across {} pages",
			owners.size(), totalElements, totalPages);

		return new PageResult<>(
			owners,
			pageRequest.page(),
			pageRequest.size(),
			totalElements,
			totalPages
		);
	}
}

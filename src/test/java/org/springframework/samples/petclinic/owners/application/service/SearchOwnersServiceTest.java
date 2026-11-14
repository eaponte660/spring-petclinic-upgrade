package org.springframework.samples.petclinic.owners.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.shared.domain.model.PageRequest;
import org.springframework.samples.petclinic.shared.domain.model.PageResult;
import org.springframework.samples.petclinic.owners.application.port.in.SearchOwnersUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.OwnerRepository;
import org.springframework.samples.petclinic.owners.domain.model.Owner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchOwnersServiceTest {

	private static final String LAST_NAME = "Doe";

	@Mock
	private OwnerRepository ownerRepository;

	@InjectMocks
	private SearchOwnersService searchOwnersService;

	@Test
	@DisplayName("Should successfully search owners by last name and return paged results")
	void shouldSearchOwnersSuccessfully() {
		// Arrange
		var pageRequest = new PageRequest(0, 10);
		var query = new SearchOwnersUseCase.SearchOwnersQuery(LAST_NAME, pageRequest);

		var owner = new Owner(1L, "John", LAST_NAME, "Street", "City", "1234567890", List.of());
		List<Owner> ownersList = List.of(owner);

		when(ownerRepository.findByLastNameContainingIgnoreCase(LAST_NAME, pageRequest)).thenReturn(ownersList);
		when(ownerRepository.countByLastNameContainingIgnoreCase(LAST_NAME)).thenReturn(1L);

		// Act
		PageResult<Owner> result = searchOwnersService.searchOwners(query);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.content()).hasSize(1);
		assertThat(result.content().getFirst().getLastName()).isEqualTo(LAST_NAME);
		assertThat(result.totalElements()).isEqualTo(1L);
		assertThat(result.totalPages()).isEqualTo(1);

		verify(ownerRepository, times(1)).findByLastNameContainingIgnoreCase(LAST_NAME, pageRequest);
		verify(ownerRepository, times(1)).countByLastNameContainingIgnoreCase(LAST_NAME);
	}

	@Test
	@DisplayName("Should handle null last name by searching with empty string")
	void shouldHandleNullLastNameSuccessfully() {
		// Arrange
		var pageRequest = new PageRequest(0, 10);
		var query = new SearchOwnersUseCase.SearchOwnersQuery(null, pageRequest);

		var owner = new Owner(1L, "John", "Doe", "Street", "City", "1234567890", List.of());
		List<Owner> ownersList = List.of(owner);

		when(ownerRepository.findByLastNameContainingIgnoreCase("", pageRequest)).thenReturn(ownersList);
		when(ownerRepository.countByLastNameContainingIgnoreCase("")).thenReturn(1L);

		// Act
		PageResult<Owner> result = searchOwnersService.searchOwners(query);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.content()).hasSize(1);

		verify(ownerRepository, times(1)).findByLastNameContainingIgnoreCase("", pageRequest);
		verify(ownerRepository, times(1)).countByLastNameContainingIgnoreCase("");
	}
}

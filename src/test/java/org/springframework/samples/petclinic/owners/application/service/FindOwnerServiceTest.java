package org.springframework.samples.petclinic.owners.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.owners.application.port.in.FindOwnerUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.OwnerRepository;
import org.springframework.samples.petclinic.owners.domain.exception.OwnerNotFoundException;
import org.springframework.samples.petclinic.owners.domain.model.Owner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindOwnerServiceTest {

	private static final Long OWNER_ID = 1L;
	private static final String FIRST_NAME = "John";
	private static final String LAST_NAME = "Doe";
	private static final String ADDRESS = "123 Main St";
	private static final String CITY = "Springfield";
	private static final String TELEPHONE = "1234567890";

	@Mock
	private OwnerRepository ownerRepository;

	@InjectMocks
	private FindOwnerService findOwnerService;

	@Test
	@DisplayName("Should successfully find and return owner by ID")
	void shouldFindOwnerByIdSuccessfully() {
		// Arrange
		var query = new FindOwnerUseCase.FindOwnerQuery(OWNER_ID);
		var expectedOwner = new Owner(
			OWNER_ID,
			FIRST_NAME,
			LAST_NAME,
			ADDRESS,
			CITY,
			TELEPHONE
		);

		when(ownerRepository.findById(OWNER_ID)).thenReturn(Optional.of(expectedOwner));

		// Act
		Owner result = findOwnerService.findOwnerById(query);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(OWNER_ID);
		assertThat(result.getFirstName()).isEqualTo(FIRST_NAME);
		assertThat(result.getLastName()).isEqualTo(LAST_NAME);
		assertThat(result.getAddress()).isEqualTo(ADDRESS);
		assertThat(result.getCity()).isEqualTo(CITY);
		assertThat(result.getTelephone()).isEqualTo(TELEPHONE);

		verify(ownerRepository, times(1)).findById(OWNER_ID);
	}

	@Test
	@DisplayName("Should throw OwnerNotFoundException when owner does not exist")
	void shouldThrowExceptionWhenOwnerNotFound() {
		// Arrange
		var query = new FindOwnerUseCase.FindOwnerQuery(OWNER_ID);
		when(ownerRepository.findById(OWNER_ID)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> findOwnerService.findOwnerById(query))
			.isInstanceOf(OwnerNotFoundException.class)
			.hasMessageContaining("Owner not found with id: " + OWNER_ID);

		verify(ownerRepository, times(1)).findById(OWNER_ID);
	}
}

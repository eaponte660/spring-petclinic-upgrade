package org.springframework.samples.petclinic.owners.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.owners.application.port.in.PageRequest;
import org.springframework.samples.petclinic.owners.application.port.in.PageResult;
import org.springframework.samples.petclinic.owners.application.port.out.OwnerRepository;
import org.springframework.samples.petclinic.owners.application.port.out.PetRepository;
import org.springframework.samples.petclinic.owners.domain.exception.OwnerNotFoundException;
import org.springframework.samples.petclinic.owners.domain.model.Owner;
import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.samples.petclinic.owners.domain.model.PetType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPetsByOwnerServiceTest {

	private static final Long OWNER_ID = 1L;

	@Mock
	private PetRepository petRepository;

	@Mock
	private OwnerRepository ownerRepository;

	@InjectMocks
	private FindPetsByOwnerService findPetsByOwnerService;

	@Test
	@DisplayName("Should successfully find paged pets by owner ID")
	void shouldFindPetsByOwnerSuccessfully() {
		// Arrange
		var pageRequest = new PageRequest(0, 10);
		var owner = new Owner(OWNER_ID, "John", "Doe", "Street", "City", "1234567890");
		var petType = new PetType(1L, "Dog");
		var pet = new Pet(10L, "Firulais", LocalDate.of(2020, 1, 1), petType, OWNER_ID);

		var pageResult = new PageResult<>(List.of(pet), 0, 10, 1L, 2);

		when(ownerRepository.findById(OWNER_ID)).thenReturn(Optional.of(owner));
		when(petRepository.findByOwnerId(OWNER_ID, pageRequest)).thenReturn(pageResult);

		// Act
		PageResult<Pet> result = findPetsByOwnerService.findPetsByOwner(OWNER_ID, pageRequest);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.content()).hasSize(1);
		assertThat(result.content().getFirst().getName()).isEqualTo("Firulais");
		assertThat(result.totalElements()).isEqualTo(1L);

		verify(ownerRepository, times(1)).findById(OWNER_ID);
		verify(petRepository, times(1)).findByOwnerId(OWNER_ID, pageRequest);
	}

	@Test
	@DisplayName("Should throw OwnerNotFoundException when owner does not exist")
	void shouldThrowExceptionWhenOwnerNotFound() {
		// Arrange
		var pageRequest = new PageRequest(0, 10);
		when(ownerRepository.findById(OWNER_ID)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> findPetsByOwnerService.findPetsByOwner(OWNER_ID, pageRequest))
			.isInstanceOf(OwnerNotFoundException.class)
			.hasMessageContaining("Owner not found with id: " + OWNER_ID);

		verify(ownerRepository, times(1)).findById(OWNER_ID);
		verifyNoInteractions(petRepository);
	}
}

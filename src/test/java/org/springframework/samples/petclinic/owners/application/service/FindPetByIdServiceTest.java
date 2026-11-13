package org.springframework.samples.petclinic.owners.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.owners.application.port.in.FindPetByIdUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.PetRepository;
import org.springframework.samples.petclinic.owners.domain.exception.PetNotFoundException;
import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.samples.petclinic.owners.domain.model.PetType;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPetByIdServiceTest {

	private static final Long PET_ID = 1L;
	private static final Long OWNER_ID = 10L;
	private static final String PET_NAME = "Firulais";
	private static final LocalDate BIRTH_DATE = LocalDate.of(2021, 3, 10);

	@Mock
	private PetRepository petRepository;

	@InjectMocks
	private FindPetByIdService findPetByIdService;

	@Test
	@DisplayName("Should successfully find and return pet by ID")
	void shouldFindPetByIdSuccessfully() {
		// Arrange
		var query = new FindPetByIdUseCase.FindPetQuery(PET_ID, OWNER_ID);
		var petType = new PetType(1L, "Dog");
		var expectedPet = new Pet(PET_ID, PET_NAME, BIRTH_DATE, petType, OWNER_ID);

		when(petRepository.findById(PET_ID)).thenReturn(Optional.of(expectedPet));

		// Act
		Pet result = findPetByIdService.findPetById(query);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(PET_ID);
		assertThat(result.getName()).isEqualTo(PET_NAME);
		assertThat(result.getOwnerId()).isEqualTo(OWNER_ID);
		assertThat(result.getType()).isEqualTo(petType);

		verify(petRepository, times(1)).findById(PET_ID);
	}

	@Test
	@DisplayName("Should throw PetNotFoundException when pet does not exist")
	void shouldThrowExceptionWhenPetNotFound() {
		// Arrange
		var query = new FindPetByIdUseCase.FindPetQuery(PET_ID, OWNER_ID);
		when(petRepository.findById(PET_ID)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> findPetByIdService.findPetById(query))
			.isInstanceOf(PetNotFoundException.class)
			.hasMessageContaining("Pet not found with id: " + PET_ID);

		verify(petRepository, times(1)).findById(PET_ID);
	}
}

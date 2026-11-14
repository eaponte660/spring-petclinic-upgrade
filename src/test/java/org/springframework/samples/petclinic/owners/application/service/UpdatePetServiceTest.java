package org.springframework.samples.petclinic.owners.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.owners.application.port.in.UpdatePetUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.PetRepository;
import org.springframework.samples.petclinic.owners.application.port.out.PetTypeRepository;
import org.springframework.samples.petclinic.owners.domain.exception.DuplicatePetNameException;
import org.springframework.samples.petclinic.owners.domain.exception.PetNotFoundException;
import org.springframework.samples.petclinic.owners.domain.exception.PetTypeNotFoundException;
import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.samples.petclinic.owners.domain.model.PetType;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdatePetServiceTest {

	private static final Long PET_ID = 1L;
	private static final Long OWNER_ID = 10L;
	private static final Long TYPE_ID = 2L;
	private static final String PET_NAME = "Firulais Updated";
	private static final LocalDate BIRTH_DATE = LocalDate.of(2019, 6, 20);

	@Mock
	private PetRepository petRepository;

	@Mock
	private PetTypeRepository petTypeRepository;

	@InjectMocks
	private UpdatePetService updatePetService;

	@Test
	@DisplayName("Should successfully update and return an existing pet")
	void shouldUpdatePetSuccessfully() {
		// Arrange
		var command = new UpdatePetUseCase.UpdatePetCommand(PET_ID, PET_NAME, BIRTH_DATE, TYPE_ID, OWNER_ID);
		var oldPetType = new PetType(1L, "Cat");
		var newPetType = new PetType(TYPE_ID, "Dog");
		var existingPet = new Pet(PET_ID, "OldName", LocalDate.of(2018, 1, 1), oldPetType, OWNER_ID);

		when(petRepository.findById(PET_ID)).thenReturn(Optional.of(existingPet));
		when(petRepository.existsByOwnerIdAndNameIgnoreCaseAndIdNot(OWNER_ID, PET_NAME, PET_ID)).thenReturn(false);
		when(petTypeRepository.findById(TYPE_ID)).thenReturn(Optional.of(newPetType));
		when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		Pet result = updatePetService.updatePet(command);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(PET_ID);
		assertThat(result.getName()).isEqualTo(PET_NAME);
		assertThat(result.getBirthDate()).isEqualTo(BIRTH_DATE);
		assertThat(result.getType()).isEqualTo(newPetType);
		assertThat(result.getOwnerId()).isEqualTo(OWNER_ID);

		verify(petRepository).findById(PET_ID);
		verify(petRepository).existsByOwnerIdAndNameIgnoreCaseAndIdNot(OWNER_ID, PET_NAME, PET_ID);
		verify(petTypeRepository).findById(TYPE_ID);
		verify(petRepository).save(any(Pet.class));
	}

	@Test
	@DisplayName("Should throw PetNotFoundException when pet to update does not exist")
	void shouldThrowExceptionWhenPetNotFound() {
		// Arrange
		var command = new UpdatePetUseCase.UpdatePetCommand(PET_ID, PET_NAME, BIRTH_DATE, TYPE_ID, OWNER_ID);
		when(petRepository.findById(PET_ID)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> updatePetService.updatePet(command))
			.isInstanceOf(PetNotFoundException.class)
			.hasMessageContaining("Pet not found with id: " + PET_ID);

		verify(petRepository).findById(PET_ID);
		verifyNoInteractions(petTypeRepository);
	}

	@Test
	@DisplayName("Should throw PetNotFoundException when pet does not belong to the specified owner")
	void shouldThrowExceptionWhenPetDoesNotBelongToOwner() {
		// Arrange
		var command = new UpdatePetUseCase.UpdatePetCommand(PET_ID, PET_NAME, BIRTH_DATE, TYPE_ID, OWNER_ID);
		var existingPet = new Pet(PET_ID, "Firulais", LocalDate.of(2018, 1, 1), new PetType(1L, "Cat"), 99L); // Pertenece a otro dueño (99L)

		when(petRepository.findById(PET_ID)).thenReturn(Optional.of(existingPet));

		// Act & Assert
		assertThatThrownBy(() -> updatePetService.updatePet(command))
			.isInstanceOf(PetNotFoundException.class)
			.hasMessageContaining("The pet does not belong to the specified owner");

		verify(petRepository).findById(PET_ID);
		verify(petRepository, never()).save(any(Pet.class));
	}

	@Test
	@DisplayName("Should throw DuplicatePetNameException when another pet with same name exists for owner")
	void shouldThrowExceptionWhenPetNameAlreadyExists() {
		// Arrange
		var command = new UpdatePetUseCase.UpdatePetCommand(PET_ID, PET_NAME, BIRTH_DATE, TYPE_ID, OWNER_ID);
		var existingPet = new Pet(PET_ID, "Firulais", LocalDate.of(2018, 1, 1), new PetType(1L, "Cat"), OWNER_ID);

		when(petRepository.findById(PET_ID)).thenReturn(Optional.of(existingPet));
		when(petRepository.existsByOwnerIdAndNameIgnoreCaseAndIdNot(OWNER_ID, PET_NAME, PET_ID)).thenReturn(true);

		// Act & Assert
		assertThatThrownBy(() -> updatePetService.updatePet(command))
			.isInstanceOf(DuplicatePetNameException.class)
			.hasMessageContaining("A pet named '" + PET_NAME + "' already exists for this owner");

		verify(petRepository).findById(PET_ID);
		verify(petRepository).existsByOwnerIdAndNameIgnoreCaseAndIdNot(OWNER_ID, PET_NAME, PET_ID);
		verifyNoInteractions(petTypeRepository);
	}

	@Test
	@DisplayName("Should throw PetTypeNotFoundException when updated pet type does not exist")
	void shouldThrowExceptionWhenPetTypeNotFound() {
		// Arrange
		var command = new UpdatePetUseCase.UpdatePetCommand(PET_ID, PET_NAME, BIRTH_DATE, TYPE_ID, OWNER_ID);
		var existingPet = new Pet(PET_ID, "Firulais", LocalDate.of(2018, 1, 1), new PetType(1L, "Cat"), OWNER_ID);

		when(petRepository.findById(PET_ID)).thenReturn(Optional.of(existingPet));
		when(petRepository.existsByOwnerIdAndNameIgnoreCaseAndIdNot(OWNER_ID, PET_NAME, PET_ID)).thenReturn(false);
		when(petTypeRepository.findById(TYPE_ID)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> updatePetService.updatePet(command))
			.isInstanceOf(PetTypeNotFoundException.class)
			.hasMessageContaining("Pet type not found with id: " + TYPE_ID);

		verify(petRepository).findById(PET_ID);
		verify(petTypeRepository).findById(TYPE_ID);
		verify(petRepository, never()).save(any(Pet.class));
	}
}

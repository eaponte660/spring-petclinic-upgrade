package org.springframework.samples.petclinic.owners.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.owners.application.port.in.CreatePetUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.OwnerRepository;
import org.springframework.samples.petclinic.owners.application.port.out.PetRepository;
import org.springframework.samples.petclinic.owners.application.port.out.PetTypeRepository;
import org.springframework.samples.petclinic.owners.domain.exception.DuplicatePetNameException;
import org.springframework.samples.petclinic.owners.domain.exception.OwnerNotFoundException;
import org.springframework.samples.petclinic.owners.domain.exception.PetTypeNotFoundException;
import org.springframework.samples.petclinic.owners.domain.model.Owner;
import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.samples.petclinic.owners.domain.model.PetType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePetServiceTest {

	private static final Long OWNER_ID = 1L;
	private static final Long PET_ID = 10L;
	private static final Long TYPE_ID = 2L;
	private static final String PET_NAME = "Firulais";
	private static final String FIRST_NAME = "John";
	private static final String LAST_NAME = "Doe";
	private static final String ADDRESS = "123 Main St";
	private static final String CITY = "Springfield";
	private static final String TELEPHONE = "1234567890";
	private static final LocalDate BIRTH_DATE = LocalDate.of(2020, 5, 15);

	@Mock
	private PetRepository petRepository;

	@Mock
	private OwnerRepository ownerRepository;

	@Mock
	private PetTypeRepository petTypeRepository;

	@InjectMocks
	private CreatePetService createPetService;

	@Test
	@DisplayName("Should successfully create and return a new pet")
	void shouldCreatePetSuccessfully() {
		// Arrange
		var command = new CreatePetUseCase.CreatePetCommand(OWNER_ID, PET_NAME, BIRTH_DATE, TYPE_ID);
		var owner = new Owner(OWNER_ID, FIRST_NAME, LAST_NAME, ADDRESS, CITY, TELEPHONE, List.of());
		var petType = new PetType(TYPE_ID, "Dog");
		var savedPet = new Pet(PET_ID, PET_NAME, BIRTH_DATE, petType, OWNER_ID);

		when(ownerRepository.findById(OWNER_ID)).thenReturn(Optional.of(owner));
		when(petRepository.existsByOwnerIdAndNameIgnoreCase(OWNER_ID, PET_NAME)).thenReturn(false);
		when(petTypeRepository.findById(TYPE_ID)).thenReturn(Optional.of(petType));
		when(petRepository.save(any(Pet.class))).thenReturn(savedPet);

		// Act
		Pet result = createPetService.createPet(command);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(PET_ID);
		assertThat(result.getName()).isEqualTo(PET_NAME);
		assertThat(result.getOwnerId()).isEqualTo(OWNER_ID);
		assertThat(result.getType()).isEqualTo(petType);

		verify(ownerRepository).findById(OWNER_ID);
		verify(petRepository).existsByOwnerIdAndNameIgnoreCase(OWNER_ID, PET_NAME);
		verify(petTypeRepository).findById(TYPE_ID);
		verify(petRepository).save(any(Pet.class));
	}

	@Test
	@DisplayName("Should throw OwnerNotFoundException when owner does not exist")
	void shouldThrowExceptionWhenOwnerNotFound() {
		// Arrange
		var command = new CreatePetUseCase.CreatePetCommand(OWNER_ID, PET_NAME, BIRTH_DATE, TYPE_ID);
		when(ownerRepository.findById(OWNER_ID)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> createPetService.createPet(command))
			.isInstanceOf(OwnerNotFoundException.class)
			.hasMessageContaining("Owner not found with id: " + OWNER_ID);

		verify(ownerRepository).findById(OWNER_ID);
		verifyNoInteractions(petRepository, petTypeRepository);
	}

	@Test
	@DisplayName("Should throw DuplicatePetNameException when pet name already exists for owner")
	void shouldThrowExceptionWhenPetNameAlreadyExists() {
		// Arrange
		var command = new CreatePetUseCase.CreatePetCommand(OWNER_ID, PET_NAME, BIRTH_DATE, TYPE_ID);
		var owner = new Owner(OWNER_ID, FIRST_NAME, LAST_NAME, ADDRESS, CITY, TELEPHONE, List.of());

		when(ownerRepository.findById(OWNER_ID)).thenReturn(Optional.of(owner));
		when(petRepository.existsByOwnerIdAndNameIgnoreCase(OWNER_ID, PET_NAME)).thenReturn(true);

		// Act & Assert
		assertThatThrownBy(() -> createPetService.createPet(command))
			.isInstanceOf(DuplicatePetNameException.class)
			.hasMessageContaining("A pet named '" + PET_NAME + "' already exists for this owner");

		verify(ownerRepository).findById(OWNER_ID);
		verify(petRepository).existsByOwnerIdAndNameIgnoreCase(OWNER_ID, PET_NAME);
		verifyNoInteractions(petTypeRepository);
	}

	@Test
	@DisplayName("Should throw PetTypeNotFoundException when pet type does not exist")
	void shouldThrowExceptionWhenPetTypeNotFound() {
		// Arrange
		var command = new CreatePetUseCase.CreatePetCommand(OWNER_ID, PET_NAME, BIRTH_DATE, TYPE_ID);
		var owner = new Owner(OWNER_ID, FIRST_NAME, LAST_NAME, ADDRESS, CITY, TELEPHONE, List.of());

		when(ownerRepository.findById(OWNER_ID)).thenReturn(Optional.of(owner));
		when(petRepository.existsByOwnerIdAndNameIgnoreCase(OWNER_ID, PET_NAME)).thenReturn(false);
		when(petTypeRepository.findById(TYPE_ID)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> createPetService.createPet(command))
			.isInstanceOf(PetTypeNotFoundException.class)
			.hasMessageContaining("Pet type not found with id: " + TYPE_ID);

		verify(ownerRepository).findById(OWNER_ID);
		verify(petRepository).existsByOwnerIdAndNameIgnoreCase(OWNER_ID, PET_NAME);
		verify(petTypeRepository).findById(TYPE_ID);
		verify(petRepository, never()).save(any(Pet.class));
	}
}

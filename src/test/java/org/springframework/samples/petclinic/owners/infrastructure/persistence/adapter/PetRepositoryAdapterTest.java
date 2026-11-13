package org.springframework.samples.petclinic.owners.infrastructure.persistence.adapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.samples.petclinic.owners.application.port.in.PageRequest;
import org.springframework.samples.petclinic.owners.application.port.in.PageResult;
import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.samples.petclinic.owners.domain.model.PetType;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.OwnerJpaEntity;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.PetJpaEntity;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.PetTypeJpaEntity;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.mapper.PetEntityMapper;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.repository.SpringDataOwnerRepository;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.repository.SpringDataPetRepository;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.repository.SpringDataPetTypeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetRepositoryAdapterTest {

	private static final Long PET_ID = 1L;
	private static final Long OWNER_ID = 10L;
	private static final Long TYPE_ID = 2L;
	private static final String PET_NAME = "Firulais";
	private static final LocalDate BIRTH_DATE = LocalDate.of(2020, 5, 15);

	@Mock
	private SpringDataPetRepository springDataPetRepository;

	@Mock
	private SpringDataOwnerRepository springDataOwnerRepository;

	@Mock
	private SpringDataPetTypeRepository springDataPetTypeRepository;

	@Mock
	private PetEntityMapper petEntityMapper;

	@InjectMocks
	private PetRepositoryAdapter petRepositoryAdapter;

	@Test
	@DisplayName("Should successfully save pet when owner and type exist")
	void shouldSavePetSuccessfully() {
		// Arrange
		var petType = new PetType(TYPE_ID, "Dog");
		var domainPet = new Pet(null, PET_NAME, BIRTH_DATE, petType, OWNER_ID);

		var ownerEntity = new OwnerJpaEntity();
		var typeEntity = new PetTypeJpaEntity();
		var petEntity = new PetJpaEntity();
		var savedPetEntity = new PetJpaEntity();
		var expectedDomainPet = new Pet(PET_ID, PET_NAME, BIRTH_DATE, petType, OWNER_ID);

		when(springDataOwnerRepository.findById(OWNER_ID)).thenReturn(Optional.of(ownerEntity));
		when(springDataPetTypeRepository.findById(TYPE_ID)).thenReturn(Optional.of(typeEntity));
		when(petEntityMapper.toEntity(domainPet, ownerEntity, typeEntity)).thenReturn(petEntity);
		when(springDataPetRepository.save(petEntity)).thenReturn(savedPetEntity);
		when(petEntityMapper.toDomain(savedPetEntity)).thenReturn(expectedDomainPet);

		// Act
		Pet result = petRepositoryAdapter.save(domainPet);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(PET_ID);
		assertThat(result.getName()).isEqualTo(PET_NAME);

		verify(springDataOwnerRepository).findById(OWNER_ID);
		verify(springDataPetTypeRepository).findById(TYPE_ID);
		verify(petEntityMapper).toEntity(domainPet, ownerEntity, typeEntity);
		verify(springDataPetRepository).save(petEntity);
		verify(petEntityMapper).toDomain(savedPetEntity);
	}

	@Test
	@DisplayName("Should throw IllegalArgumentException when owner not found during save")
	void shouldThrowExceptionWhenOwnerNotFoundOnSave() {
		// Arrange
		var petType = new PetType(TYPE_ID, "Dog");
		var domainPet = new Pet(null, PET_NAME, BIRTH_DATE, petType, OWNER_ID);

		when(springDataOwnerRepository.findById(OWNER_ID)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> petRepositoryAdapter.save(domainPet))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Owner not found with id: " + OWNER_ID);

		verify(springDataOwnerRepository).findById(OWNER_ID);
		verifyNoInteractions(springDataPetTypeRepository, springDataPetRepository);
	}

	@Test
	@DisplayName("Should find pet by ID and return domain model when found")
	void shouldFindPetByIdSuccessfully() {
		// Arrange
		var petEntity = new PetJpaEntity();
		var petType = new PetType(TYPE_ID, "Dog");
		var expectedDomainPet = new Pet(PET_ID, PET_NAME, BIRTH_DATE, petType, OWNER_ID);

		when(springDataPetRepository.findById(PET_ID)).thenReturn(Optional.of(petEntity));
		when(petEntityMapper.toDomain(petEntity)).thenReturn(expectedDomainPet);

		// Act
		Optional<Pet> result = petRepositoryAdapter.findById(PET_ID);

		// Assert
		assertThat(result).isPresent();
		assertThat(result.get().getId()).isEqualTo(PET_ID);

		verify(springDataPetRepository).findById(PET_ID);
		verify(petEntityMapper).toDomain(petEntity);
	}

	@Test
	@DisplayName("Should find paged pets by owner ID successfully")
	void shouldFindByOwnerIdSuccessfully() {
		// Arrange
		var sharedPageRequest = new PageRequest(0, 10);
		Pageable expectedPageable = org.springframework.data.domain.PageRequest.of(0, 10, Sort.by("name").ascending());

		var petEntity = new PetJpaEntity();
		var petType = new PetType(TYPE_ID, "Dog");
		var expectedDomainPet = new Pet(PET_ID, PET_NAME, BIRTH_DATE, petType, OWNER_ID);

		var pageEntity = new PageImpl<>(List.of(petEntity), expectedPageable, 1L);

		when(springDataPetRepository.findByOwnerId(eq(OWNER_ID), eq(expectedPageable))).thenReturn(pageEntity);
		when(petEntityMapper.toDomain(petEntity)).thenReturn(expectedDomainPet);

		// Act
		PageResult<Pet> result = petRepositoryAdapter.findByOwnerId(OWNER_ID, sharedPageRequest);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.content()).hasSize(1);
		assertThat(result.totalElements()).isEqualTo(1L);
		assertThat(result.content().getFirst().getName()).isEqualTo(PET_NAME);

		verify(springDataPetRepository).findByOwnerId(eq(OWNER_ID), eq(expectedPageable));
		verify(petEntityMapper).toDomain(petEntity);
	}

	@Test
	@DisplayName("Should check existence by owner ID and name ignore case")
	void shouldCheckExistsByOwnerIdAndNameIgnoreCase() {
		// Arrange
		when(springDataPetRepository.existsByOwnerIdAndNameIgnoreCase(OWNER_ID, PET_NAME)).thenReturn(true);

		// Act
		boolean exists = petRepositoryAdapter.existsByOwnerIdAndNameIgnoreCase(OWNER_ID, PET_NAME);

		// Assert
		assertThat(exists).isTrue();
		verify(springDataPetRepository).existsByOwnerIdAndNameIgnoreCase(OWNER_ID, PET_NAME);
	}

	@Test
	@DisplayName("Should check existence by owner ID, name ignore case and ID not")
	void shouldCheckExistsByOwnerIdAndNameIgnoreCaseAndIdNot() {
		// Arrange
		when(springDataPetRepository.existsByOwnerIdAndNameIgnoreCaseAndIdNot(OWNER_ID, PET_NAME, PET_ID)).thenReturn(false);

		// Act
		boolean exists = petRepositoryAdapter.existsByOwnerIdAndNameIgnoreCaseAndIdNot(OWNER_ID, PET_NAME, PET_ID);

		// Assert
		assertThat(exists).isFalse();
		verify(springDataPetRepository).existsByOwnerIdAndNameIgnoreCaseAndIdNot(OWNER_ID, PET_NAME, PET_ID);
	}
}

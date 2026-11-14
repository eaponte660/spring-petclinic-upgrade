package org.springframework.samples.petclinic.owners.infrastructure.persistence.adapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.owners.domain.model.PetType;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.PetTypeJpaEntity;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.mapper.PetTypeEntityMapper;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.repository.SpringDataPetTypeRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetTypeRepositoryAdapterTest {

	@Mock
	private SpringDataPetTypeRepository springDataPetTypeRepository;

	@Mock
	private PetTypeEntityMapper petTypeEntityMapper;

	@InjectMocks
	private PetTypeRepositoryAdapter petTypeRepositoryAdapter;

	@Test
	@DisplayName("Should return pet type when ID exists")
	void shouldReturnPetTypeWhenIdExists() {
		// Arrange
		var id = 1L;
		var entity = new PetTypeJpaEntity();
		entity.setId(id);
		entity.setName("cat");

		var domain = new PetType(id, "cat");

		when(springDataPetTypeRepository.findById(id)).thenReturn(Optional.of(entity));
		when(petTypeEntityMapper.toDomain(entity)).thenReturn(domain);

		// Act
		var result = petTypeRepositoryAdapter.findById(id);

		// Assert
		assertThat(result).isPresent();
		assertThat(result.get().id()).isEqualTo(id);
		assertThat(result.get().name()).isEqualTo("cat");

		verify(springDataPetTypeRepository).findById(id);
		verify(petTypeEntityMapper).toDomain(entity);
	}

	@Test
	@DisplayName("Should return empty when pet type ID does not exist")
	void shouldReturnEmptyWhenIdDoesNotExist() {
		// Arrange
		var id = 99L;
		when(springDataPetTypeRepository.findById(id)).thenReturn(Optional.empty());

		// Act
		var result = petTypeRepositoryAdapter.findById(id);

		// Assert
		assertThat(result).isEmpty();

		verify(springDataPetTypeRepository).findById(id);
		verifyNoInteractions(petTypeEntityMapper);
	}

	@Test
	void shouldReturnAllPetTypesMappedToDomain() {
		// Given
		PetTypeJpaEntity jpaEntity = new PetTypeJpaEntity();
		jpaEntity.setId(1L);
		jpaEntity.setName("dog");

		PetType domainModel = new PetType(1L, "dog");

		when(springDataPetTypeRepository.findAll()).thenReturn(List.of(jpaEntity));
		when(petTypeEntityMapper.toDomain(jpaEntity)).thenReturn(domainModel);

		// When
		List<PetType> result = petTypeRepositoryAdapter.findAll();

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(1L, result.getFirst().id());
		assertEquals("dog", result.getFirst().name());

		verify(springDataPetTypeRepository, times(1)).findAll();
		verify(petTypeEntityMapper, times(1)).toDomain(jpaEntity);
	}
}

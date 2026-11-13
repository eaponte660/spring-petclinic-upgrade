package org.springframework.samples.petclinic.vets.infrastructure.persistence.adapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.shared.domain.model.PageRequest;
import org.springframework.samples.petclinic.vets.domain.model.Specialty;
import org.springframework.samples.petclinic.vets.domain.model.Vet;
import org.springframework.samples.petclinic.vets.infrastructure.persistence.entity.VetEntity;
import org.springframework.samples.petclinic.vets.infrastructure.persistence.mapper.VetEntityMapper;
import org.springframework.samples.petclinic.vets.infrastructure.persistence.repository.SpringDataVetRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VetRepositoryAdapterTest {

	@Mock
	private SpringDataVetRepository springDataVetRepository;

	@Mock
	private VetEntityMapper vetEntityMapper;

	@InjectMocks
	private VetRepositoryAdapter vetRepositoryAdapter;

	@Test
	@DisplayName("Should find vet by ID successfully")
	void shouldFindVetByIdSuccessfully() {
		// Arrange
		var vetId = 1L;
		var vetEntity = new VetEntity();
		vetEntity.setId(vetId);
		vetEntity.setFirstName("James");
		vetEntity.setLastName("Carter");

		var specialty = new Specialty(1L, "radiology");
		var domainVet = new Vet(vetId, "James", "Carter", List.of(specialty));

		when(springDataVetRepository.findById(vetId)).thenReturn(Optional.of(vetEntity));
		when(vetEntityMapper.toDomain(vetEntity)).thenReturn(domainVet);

		// Act
		var result = vetRepositoryAdapter.findById(vetId);

		// Assert
		assertThat(result).isPresent();
		assertThat(result.get().id()).isEqualTo(vetId);
		assertThat(result.get().firstName()).isEqualTo("James");

		verify(springDataVetRepository).findById(vetId);
		verify(vetEntityMapper).toDomain(vetEntity);
	}

	@Test
	@DisplayName("Should return empty when vet is not found by ID")
	void shouldReturnEmptyWhenVetNotFoundById() {
		// Arrange
		var vetId = 99L;
		when(springDataVetRepository.findById(vetId)).thenReturn(Optional.empty());

		// Act
		var result = vetRepositoryAdapter.findById(vetId);

		// Assert
		assertThat(result).isEmpty();
		verify(springDataVetRepository).findById(vetId);
		verifyNoInteractions(vetEntityMapper);
	}

	@Test
	@DisplayName("Should find all vets with pagination successfully")
	void shouldFindAllVetsWithPaginationSuccessfully() {
		// Arrange
		var pageRequest = new PageRequest(0, 10);
		var vetEntity = new VetEntity();
		vetEntity.setId(1L);
		vetEntity.setFirstName("Helen");
		vetEntity.setLastName("Leary");

		var springPage = new PageImpl<>(List.of(vetEntity), org.springframework.data.domain.PageRequest.of(0, 10), 1);
		var domainVet = new Vet(1L, "Helen", "Leary", List.of());

		when(springDataVetRepository.findAll(any(Pageable.class))).thenReturn(springPage);
		when(vetEntityMapper.toDomain(vetEntity)).thenReturn(domainVet);

		// Act
		var result = vetRepositoryAdapter.findAll(pageRequest);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.content()).hasSize(1);
		assertThat(result.totalElements()).isEqualTo(1L);
		assertThat(result.content().get(0).firstName()).isEqualTo("Helen");

		verify(springDataVetRepository).findAll(any(Pageable.class));
		verify(vetEntityMapper).toDomain(vetEntity);
	}
}

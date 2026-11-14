package org.springframework.samples.petclinic.visits.infrastructure.persistence.adapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.visits.domain.model.Visit;
import org.springframework.samples.petclinic.visits.infrastructure.persistence.entity.VisitEntity;
import org.springframework.samples.petclinic.visits.infrastructure.persistence.mapper.VisitEntityMapper;
import org.springframework.samples.petclinic.visits.infrastructure.persistence.repository.SpringDataVisitRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisitRepositoryAdapterTest {

	@Mock
	private SpringDataVisitRepository springDataVisitRepository;

	@Mock
	private VisitEntityMapper visitEntityMapper;

	@InjectMocks
	private VisitRepositoryAdapter visitRepositoryAdapter;

	@Test
	@DisplayName("Should save visit successfully via adapter")
	void shouldSaveVisitSuccessfully() {
		// Arrange
		var visit = new Visit(null, LocalDate.now(), "Surgery", 1L);
		var entity = new VisitEntity();
		var savedEntity = new VisitEntity();
		savedEntity.setId(1L);

		when(visitEntityMapper.toEntity(visit)).thenReturn(entity);
		when(springDataVisitRepository.save(entity)).thenReturn(savedEntity);
		when(visitEntityMapper.toDomain(savedEntity)).thenReturn(new Visit(1L, visit.date(), visit.description(), visit.petId()));

		// Act
		var result = visitRepositoryAdapter.save(visit);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.id()).isEqualTo(1L);

		verify(springDataVisitRepository).save(entity);
	}

	@Test
	@DisplayName("Should find visits by pet ID via adapter")
	void shouldFindVisitsByPetId() {
		// Arrange
		var petId = 1L;
		var entity = new VisitEntity();
		entity.setId(1L);
		entity.setPetId(petId);

		when(springDataVisitRepository.findByPetId(petId)).thenReturn(List.of(entity));
		when(visitEntityMapper.toDomain(entity)).thenReturn(new Visit(1L, LocalDate.now(), "Checkup", petId));

		// Act
		var result = visitRepositoryAdapter.findByPetId(petId);

		// Assert
		assertThat(result).hasSize(1);
		assertThat(result.getFirst().petId()).isEqualTo(petId);

		verify(springDataVisitRepository).findByPetId(petId);
	}
}

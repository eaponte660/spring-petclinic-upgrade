package org.springframework.samples.petclinic.visits.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.visits.application.port.in.FindVisitsByPetUseCase;
import org.springframework.samples.petclinic.visits.application.port.out.VisitRepository;
import org.springframework.samples.petclinic.visits.domain.model.Visit;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindVisitsByPetServiceTest {

	@Mock
	private VisitRepository visitRepository;

	@InjectMocks
	private FindVisitsByPetService findVisitsByPetService;

	@Test
	@DisplayName("Should return visits by pet ID successfully")
	void shouldReturnVisitsByPetIdSuccessfully() {
		// Arrange
		var petId = 1L;
		var visit = new Visit(1L, LocalDate.now(), "Checkup", petId);
		when(visitRepository.findByPetId(petId)).thenReturn(List.of(visit));

		var query = new FindVisitsByPetUseCase.FindVisitsByPetQuery(petId);

		// Act
		var result = findVisitsByPetService.findVisitsByPetId(query);

		// Assert
		assertThat(result).hasSize(1);
		assertThat(result.get(0).petId()).isEqualTo(petId);
		assertThat(result.get(0).description()).isEqualTo("Checkup");

		verify(visitRepository).findByPetId(petId);
	}
}

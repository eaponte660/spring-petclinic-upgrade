package org.springframework.samples.petclinic.visits.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.visits.application.port.in.CreateVisitUseCase;
import org.springframework.samples.petclinic.visits.application.port.out.VisitRepository;
import org.springframework.samples.petclinic.visits.domain.model.Visit;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateVisitServiceTest {

	@Mock
	private VisitRepository visitRepository;

	@InjectMocks
	private CreateVisitService createVisitService;

	@Test
	@DisplayName("Should create visit successfully")
	void shouldCreateVisitSuccessfully() {
		// Arrange
		var today = LocalDate.now();
		var command = new CreateVisitUseCase.CreateVisitCommand(today, "Rabies shot", 1L);
		var savedVisit = new Visit(1L, today, "Rabies shot", 1L);

		when(visitRepository.save(any(Visit.class))).thenReturn(savedVisit);

		// Act
		var result = createVisitService.createVisit(command);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.id()).isEqualTo(1L);
		assertThat(result.description()).isEqualTo("Rabies shot");
		assertThat(result.petId()).isEqualTo(1L);

		verify(visitRepository).save(any(Visit.class));
	}
}

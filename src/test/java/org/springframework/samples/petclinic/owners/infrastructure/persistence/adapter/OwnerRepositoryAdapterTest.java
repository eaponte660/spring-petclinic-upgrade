package org.springframework.samples.petclinic.owners.infrastructure.persistence.adapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.samples.petclinic.owners.application.port.in.PageRequest;
import org.springframework.samples.petclinic.owners.domain.model.Owner;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.OwnerJpaEntity;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.mapper.OwnerEntityMapper;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.repository.SpringDataOwnerRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OwnerRepositoryAdapterTest {

	private static final Long OWNER_ID = 1L;
	private static final String FIRST_NAME = "John";
	private static final String LAST_NAME = "Doe";
	private static final String ADDRESS = "123 Main St";
	private static final String CITY = "Springfield";
	private static final String TELEPHONE = "1234567890";

	@Mock
	private SpringDataOwnerRepository springDataOwnerRepository;

	@Mock
	private OwnerEntityMapper ownerEntityMapper;

	@InjectMocks
	private OwnerRepositoryAdapter ownerRepositoryAdapter;

	@Test
	@DisplayName("Should successfully save owner by mapping to entity and back to domain")
	void shouldSaveOwnerSuccessfully() {
		// Arrange
		var domainOwner = new Owner(null, FIRST_NAME, LAST_NAME, ADDRESS, CITY, TELEPHONE);
		var ownerEntity = new OwnerJpaEntity();
		var savedEntity = new OwnerJpaEntity();
		var expectedDomainOwner = new Owner(OWNER_ID, FIRST_NAME, LAST_NAME, ADDRESS, CITY, TELEPHONE);

		when(ownerEntityMapper.toEntity(domainOwner)).thenReturn(ownerEntity);
		when(springDataOwnerRepository.save(ownerEntity)).thenReturn(savedEntity);
		when(ownerEntityMapper.toDomain(savedEntity)).thenReturn(expectedDomainOwner);

		// Act
		Owner result = ownerRepositoryAdapter.save(domainOwner);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(OWNER_ID);
		assertThat(result.getFirstName()).isEqualTo(FIRST_NAME);

		verify(ownerEntityMapper).toEntity(domainOwner);
		verify(springDataOwnerRepository).save(ownerEntity);
		verify(ownerEntityMapper).toDomain(savedEntity);
	}

	@Test
	@DisplayName("Should find owner by ID and return domain model when found")
	void shouldFindOwnerByIdSuccessfully() {
		// Arrange
		var ownerEntity = new OwnerJpaEntity();
		var expectedDomainOwner = new Owner(OWNER_ID, FIRST_NAME, LAST_NAME, ADDRESS, CITY, TELEPHONE);

		when(springDataOwnerRepository.findById(OWNER_ID)).thenReturn(Optional.of(ownerEntity));
		when(ownerEntityMapper.toDomain(ownerEntity)).thenReturn(expectedDomainOwner);

		// Act
		Optional<Owner> result = ownerRepositoryAdapter.findById(OWNER_ID);

		// Assert
		assertThat(result).isPresent();
		assertThat(result.get().getId()).isEqualTo(OWNER_ID);

		verify(springDataOwnerRepository).findById(OWNER_ID);
		verify(ownerEntityMapper).toDomain(ownerEntity);
	}

	@Test
	@DisplayName("Should return empty optional when owner ID does not exist")
	void shouldReturnEmptyWhenOwnerNotFound() {
		// Arrange
		when(springDataOwnerRepository.findById(OWNER_ID)).thenReturn(Optional.empty());

		// Act
		Optional<Owner> result = ownerRepositoryAdapter.findById(OWNER_ID);

		// Assert
		assertThat(result).isEmpty();

		verify(springDataOwnerRepository).findById(OWNER_ID);
		verifyNoInteractions(ownerEntityMapper);
	}

	@Test
	@DisplayName("Should find owners by last name containing ignore case with pagination")
	void shouldFindByLastNameContainingIgnoreCaseSuccessfully() {
		// Arrange
		var sharedPageRequest = new PageRequest(0, 10);
		var ownerEntity = new OwnerJpaEntity();
		var expectedDomainOwner = new Owner(OWNER_ID, FIRST_NAME, LAST_NAME, ADDRESS, CITY, TELEPHONE);

		Pageable expectedPageable = org.springframework.data.domain.PageRequest.of(0, 10, Sort.by("lastName").ascending());
		List<OwnerJpaEntity> ownerEntities = List.of(ownerEntity);

		when(springDataOwnerRepository.findByLastNameContainingIgnoreCase(eq(LAST_NAME), eq(expectedPageable)))
			.thenReturn(ownerEntities);
		when(ownerEntityMapper.toDomain(ownerEntity)).thenReturn(expectedDomainOwner);

		// Act
		List<Owner> result = ownerRepositoryAdapter.findByLastNameContainingIgnoreCase(LAST_NAME, sharedPageRequest);

		// Assert
		assertThat(result).hasSize(1);
		assertThat(result.getFirst().getLastName()).isEqualTo(LAST_NAME);

		verify(springDataOwnerRepository).findByLastNameContainingIgnoreCase(eq(LAST_NAME), eq(expectedPageable));
		verify(ownerEntityMapper).toDomain(ownerEntity);
	}

	@Test
	@DisplayName("Should count owners by last name containing ignore case")
	void shouldCountByLastNameContainingIgnoreCaseSuccessfully() {
		// Arrange
		when(springDataOwnerRepository.countByLastNameContainingIgnoreCase(LAST_NAME)).thenReturn(5L);

		// Act
		long count = ownerRepositoryAdapter.countByLastNameContainingIgnoreCase(LAST_NAME);

		// Assert
		assertThat(count).isEqualTo(5L);

		verify(springDataOwnerRepository).countByLastNameContainingIgnoreCase(LAST_NAME);
	}
}

package org.springframework.samples.petclinic.owners.application.port.in;

import java.util.List;

public record PageResult<T>(
	List<T> content,
	int page,
	int size,
	long totalElements,
	int totalPages
) {
	public boolean hasNext() {
		return page < totalPages - 1;
	}

	public boolean hasPrevious() {
		return page > 0;
	}
}

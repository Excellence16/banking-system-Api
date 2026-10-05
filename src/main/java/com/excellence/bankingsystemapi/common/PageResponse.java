package com.excellence.bankingsystemapi.common;

import lombok.Builder;

import java.util.List;

/**
 * Standard paginated response returned by the API.
 *
 * @param <T> Response payload type.
 */
@Builder
public record PageResponse<T>(

        // Current page content.
        List<T> content,

        // Zero-based page index.
        int page,

        // Number of records per page.
        int size,

        // Total number of matching records.
        long totalElements,

        // Total number of available pages.
        int totalPages,

        // Indicates whether this is the first page.
        boolean first,

        // Indicates whether this is the last page.
        boolean last,

        // Indicates whether another page exists after this one.
        boolean hasNext,

        // Indicates whether a previous page exists.
        boolean hasPrevious

) {

}

/*
 * Class Overview
 * --------------------------------------------------------------------
 * - Standard wrapper for paginated API responses.
 * - Works seamlessly with Spring Data Page<T>.
 * - Generic to support any response DTO.
 * - Immutable by design using Java Record.
 * - Designed for Spring Boot 3 / Java 21 enterprise applications.
 */
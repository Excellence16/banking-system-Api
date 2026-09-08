package com.excellence.bankingsystemapi.mapper;

import com.excellence.bankingsystemapi.customer.dto.response.PageResponse;
import org.springframework.data.domain.Page;

public class PageResponseMapper {

    private PageResponseMapper() {
        throw new UnsupportedOperationException(
                "Utility class cannot be instantiated"
        );
    }

    /**
    * Converts a Spring Data page into a PageResponse.
     */
    public static <T> PageResponse<T> from(Page<T> page) {

        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();

    }

}

/*
 * Class Overview
 * --------------------------------------------------------------------
 * - Converts Spring Data Page objects into PageResponse.
 * - Preserves pagination metadata.
 * - Eliminates duplicate pagination mapping logic.
 * - Generic implementation reusable across all modules.
 * - Designed for Spring Boot 3 / Java 21 enterprise applications.
 */
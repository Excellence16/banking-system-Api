package com.excellence.bankingsystemapi.common.pagination;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

public final class SortFieldValidator {

    private SortFieldValidator() {
        throw new UnsupportedOperationException(
                "Utility class cannot be instantiated."
        );
    }

    public static void validate(
            Pageable pageable,
            Set<String> allowedFields
    ) {

        for (Sort.Order order : pageable.getSort()) {

            if (!allowedFields.contains(order.getProperty())) {

                throw new IllegalArgumentException(
                        "Invalid sort field: " + order.getProperty()
                );

            }

        }

    }

}
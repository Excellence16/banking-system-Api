package com.excellence.bankingsystemapi.constants;

import com.excellence.bankingsystemapi.common.enums.SortDirection;

import java.util.Set;

/**
 * Centralized constants for customer search, pagination, and sorting.
 *
 * Keeping these values in one place makes them easy to maintain,
 * prevents magic numbers and strings, and improves consistency
 * across the application.
 */
public final class CustomerSearchConstants {

    /**
     * Prevent instantiation.
     */
    private CustomerSearchConstants() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated."
        );
    }

    /**
     * Default page number.
     */
    public static final int DEFAULT_PAGE = 0;

    /**
     * Default number of records returned per page.
     */
    public static final int DEFAULT_SIZE = 10;

    /**
     * Maximum page size allowed.
     * Prevents clients from requesting extremely large datasets.
     */
    public static final int MAX_PAGE_SIZE = 100;

    /**
     * Default field used for sorting.
     */
    public static final String DEFAULT_SORT_BY = "createdAt";

    /**
     * Default sorting direction.
     */
    public static final SortDirection DEFAULT_SORT_DIRECTION =
            SortDirection.DESC;

    /**
     * Whitelist of fields that clients are allowed to sort by.
     *
     * Never allow arbitrary field names from the client.
     */
    public static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "customerNumber",
            "firstName",
            "lastName",
            "email",
            "phoneNumber",
            "gender",
            "customerStatus",
            "createdAt",
            "updatedAt"
    );

}
package com.excellence.bankingsystemapi.constants;


import com.excellence.bankingsystemapi.common.enums.SortDirection;

import java.util.Set;

/**
 * Centralized constants for account search, filtering,
 * pagination, and sorting.
 *
 * <p>
 * Keeping these values in one place eliminates magic numbers
 * and strings, improves consistency, and simplifies maintenance.
 * </p>
 */
public final class AccountSearchConstants {

    /**
     * Prevent instantiation.
     */
    private AccountSearchConstants() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated."
        );
    }

    /**
     * Default zero-based page index.
     */
    public static final int DEFAULT_PAGE = 0;

    /**
     * Default page size.
     */
    public static final int DEFAULT_SIZE = 10;

    /**
     * Maximum number of records that may be requested
     * in a single page.
     */
    public static final int MAX_PAGE_SIZE = 100;

    /**
     * Default sort field.
     */
    public static final String DEFAULT_SORT_BY = "createdAt";

    /**
     * Default sort direction.
     */
    public static final SortDirection DEFAULT_SORT_DIRECTION =
            SortDirection.DESC;

    /**
     * Fields that clients are allowed to sort by.
     *
     * Never trust arbitrary client supplied property names.
     */
    public static final Set<String> ALLOWED_SORT_FIELDS = Set.of(

            "accountNumber",

            "customerNumber",

            "accountType",

            "accountStatus",

            "currency",

            "ledgerBalance",

            "availableBalance",

            "createdAt",

            "updatedAt"

    );

}
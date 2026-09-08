



package com.excellence.bankingsystemapi.common.pagination;

import com.excellence.bankingsystemapi.common.enums.SortDirection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

/**
 * Utility class responsible for creating validated
 * {@link Pageable} instances.
 *
 * <p>
 * This class centralizes pagination and sorting logic,
 * ensuring consistency across all application modules.
 * </p>
 *
 * <p>
 * It also validates client supplied sort fields before
 * the query reaches the repository layer.
 * </p>
 *
 * Supported Modules:
 * <ul>
 *     <li>Customer</li>
 *     <li>Account</li>
 *     <li>Transaction</li>
 *     <li>Loan</li>
 *     <li>Card</li>
 * </ul>
 *
 * @author Excellence
 * @since 1.0
 */
public final class PageableBuilder {

    /**
     * Prevent instantiation.
     */
    private PageableBuilder() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated."
        );
    }

    /**
     * Creates a validated {@link Pageable}.
     *
     * @param page zero-based page number
     * @param size page size
     * @param sortBy property used for sorting
     * @param sortDirection sorting direction
     * @param allowedSortFields whitelist of allowed sort fields
     *
     * @return validated Pageable
     */
    public static Pageable build(
            Integer page,
            Integer size,
            String sortBy,
            SortDirection sortDirection,
            Set<String> allowedSortFields
    ) {

        Pageable pageable = PageRequest.of(

                page,

                size,

                Sort.by(
                        sortDirection.toSpringDirection(),
                        sortBy
                )

        );

        SortFieldValidator.validate(
                pageable,
                allowedSortFields
        );

        return pageable;

    }

}
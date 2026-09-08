


package com.excellence.bankingsystemapi.account.dto.request;

import com.excellence.bankingsystemapi.common.enums.AccountStatus;
import com.excellence.bankingsystemapi.common.enums.AccountType;
import com.excellence.bankingsystemapi.constants.AccountSearchConstants;
import com.excellence.bankingsystemapi.common.enums.Currency;
import com.excellence.bankingsystemapi.common.enums.SortDirection;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Request DTO for account search, filtering,
 * pagination, and sorting.
 *
 * <p>
 * All search fields are optional. Only the fields supplied
 * by the client will be included in the search criteria.
 * </p>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountSearchRequestDTO {

    /**
     * Zero-based page index.
     */
    @Default
    @Min(
            value = 0,
            message = "Page number cannot be negative."
    )
    private Integer page =
            AccountSearchConstants.DEFAULT_PAGE;

    /**
     * Number of records returned per page.
     */
    @Default
    @Min(
            value = 1,
            message = "Page size must be at least 1."
    )
    @Max(
            value = AccountSearchConstants.MAX_PAGE_SIZE,
            message = "Page size cannot exceed "
                    + AccountSearchConstants.MAX_PAGE_SIZE
    )
    private Integer size =
            AccountSearchConstants.DEFAULT_SIZE;

    /**
     * Field used for sorting.
     *
     * Validation will be performed by the custom
     * {@code @ValidSortField} annotation.
     */
    @Default
    private String sortBy =
            AccountSearchConstants.DEFAULT_SORT_BY;

    /**
     * Sorting direction.
     */
    @Default
    private SortDirection sortDirection =
            AccountSearchConstants.DEFAULT_SORT_DIRECTION;

    /**
     * Search by account number.
     */
    private String accountNumber;

    /**
     * Search by customer number.
     *
     * Example:
     * CUST2026345678
     */
    private String customerNumber;

    /**
     * Filter by account type.
     */
    private AccountType accountType;

    /**
     * Filter by account status.
     */
    private AccountStatus accountStatus;

    /**
     * Filter by account currency.
     */
    private Currency currency;

    /**
     * Filter accounts created on or after this date.
     */
    private LocalDate createdDateFrom;

    /**
     * Filter accounts created on or before this date.
     */
    private LocalDate createdDateTo;

}
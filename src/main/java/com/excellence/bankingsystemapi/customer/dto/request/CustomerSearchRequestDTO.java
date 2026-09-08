package com.excellence.bankingsystemapi.customer.dto.request;

import com.excellence.bankingsystemapi.common.enums.CustomerStatus;
import com.excellence.bankingsystemapi.common.enums.Gender;
import com.excellence.bankingsystemapi.common.enums.SortDirection;
import com.excellence.bankingsystemapi.constants.CustomerSearchConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

/**
 * Request DTO for customer search, filtering,
 * sorting, and pagination.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSearchRequestDTO {

    // Zero-based page index.
    @Builder.Default
    @Min(value = 0, message = "Page number cannot be negative.")
    private Integer page = CustomerSearchConstants.DEFAULT_PAGE;

    // Number of records returned per page.
    @Builder.Default
    @Min(value = 1, message = "Page size must be at least 1.")
    @Max(
            value = CustomerSearchConstants.MAX_PAGE_SIZE,
            message = "Page size cannot exceed " + CustomerSearchConstants.MAX_PAGE_SIZE
    )
    private Integer size = CustomerSearchConstants.DEFAULT_SIZE;

    // Field to sort by.
    @Builder.Default
    private String sortBy = CustomerSearchConstants.DEFAULT_SORT_BY;

    // Sort order.
    @Builder.Default
    private SortDirection sortDirection =
            CustomerSearchConstants.DEFAULT_SORT_DIRECTION;

    // Searches across customer number, name, email, phone and BVN.
    private String keyword;

    // Filter by gender.
    private Gender gender;

    // Filter by customer status.
    private CustomerStatus status;

}
package com.excellence.bankingsystemapi.specification;

import com.excellence.bankingsystemapi.account.dto.request.AccountSearchRequestDTO;
import com.excellence.bankingsystemapi.model.Account;
import com.excellence.bankingsystemapi.model.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds dynamic JPA Specifications for Account searches.
 *
 * <p>
 * Only fields supplied by the client are included in the query.
 * Null fields are ignored.
 * </p>
 *
 * <p>
 * Supports:
 * <ul>
 *     <li>Account Number</li>
 *     <li>Customer Number</li>
 *     <li>Account Type</li>
 *     <li>Account Status</li>
 *     <li>Currency</li>
 *     <li>Created Date Range</li>
 * </ul>
 *
 * <p>
 * Designed for enterprise applications where search criteria
 * are optional and dynamically combined.
 * </p>
 *
 * @author Excellence
 * @since 1.0
 */
public final class AccountSpecification {

    /**
     * Prevent instantiation.
     */
    private AccountSpecification() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated."
        );
    }

    /**
     * Builds a dynamic {@link Specification} for Account search.
     *
     * @param request search request
     * @return Account Specification
     */
    public static Specification<Account> build(AccountSearchRequestDTO request) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            /*
             * Join Customer only once.
             * Customer Number filtering requires it.
             */
            Join<Account, Customer> customerJoin =
                    root.join("customer", JoinType.LEFT);

            addAccountNumberPredicate(request, root, criteriaBuilder, predicates);
            addCustomerNumberPredicate(request, customerJoin, criteriaBuilder, predicates);
            addAccountTypePredicate(request, root, criteriaBuilder, predicates);
            addAccountStatusPredicate(request, root, criteriaBuilder, predicates);
            addCurrencyPredicate(request, root, criteriaBuilder, predicates);
            addCreatedDatePredicate(request, root, criteriaBuilder, predicates);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Adds account number search predicate.
     */
    private static void addAccountNumberPredicate(
            AccountSearchRequestDTO request,
            Root<Account> root,
            CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates
    ) {

        if (request != null && request.getAccountNumber() != null
                && !request.getAccountNumber().isBlank()) {

            predicates.add(
                    criteriaBuilder.equal(
                            root.get("accountNumber"),
                            request.getAccountNumber().trim()
                    )
            );
        }
    }

    /**
     * Adds customer number search predicate.
     */
    private static void addCustomerNumberPredicate(
            AccountSearchRequestDTO request,
            Join<Account, Customer> customerJoin,
            CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates
    ) {

        if (request != null && request.getCustomerNumber() != null
                && !request.getCustomerNumber().isBlank()) {

            predicates.add(
                    criteriaBuilder.equal(
                            customerJoin.get("customerNumber"),
                            request.getCustomerNumber().trim()
                    )
            );
        }
    }

    /**
     * Adds account type filter predicate.
     */
    private static void addAccountTypePredicate(
            AccountSearchRequestDTO request,
            Root<Account> root,
            CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates
    ) {

        if (request != null && request.getAccountType() != null) {

            predicates.add(
                    criteriaBuilder.equal(
                            root.get("accountType"),
                            request.getAccountType()
                    )
            );
        }
    }

    /**
     * Adds account status filter predicate.
     */
    private static void addAccountStatusPredicate(
            AccountSearchRequestDTO request,
            Root<Account> root,
            CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates
    ) {

        if (request != null && request.getAccountStatus() != null) {

            predicates.add(
                    criteriaBuilder.equal(
                            root.get("accountStatus"),
                            request.getAccountStatus()
                    )
            );
        }
    }

    /**
     * Adds currency filter predicate.
     */
    private static void addCurrencyPredicate(
            AccountSearchRequestDTO request,
            Root<Account> root,
            CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates
    ) {

        if (request != null && request.getCurrency() != null) {

            predicates.add(
                    criteriaBuilder.equal(
                            root.get("currency"),
                            request.getCurrency()
                    )
            );
        }
    }

    /**
     * Adds created date range predicate.
     */
    private static void addCreatedDatePredicate(
            AccountSearchRequestDTO request,
            Root<Account> root,
            CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates
    ) {

        if (request == null) {
            return;
        }

        LocalDate createdDateFrom = request.getCreatedDateFrom();
        LocalDate createdDateTo = request.getCreatedDateTo();

        /*
         * Created Date >= createdDateFrom
         */
        if (createdDateFrom != null) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                            root.get("createdAt"),
                            createdDateFrom.atStartOfDay()
                    )
            );
        }

        /*
         * Created Date <= createdDateTo
         */
        if (createdDateTo != null) {
            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                            root.get("createdAt"),
                            createdDateTo.atTime(23, 59, 59)
                    )
            );
        }
    }
}
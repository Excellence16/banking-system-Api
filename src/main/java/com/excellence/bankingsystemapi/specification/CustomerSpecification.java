package com.excellence.bankingsystemapi.specification;





import com.excellence.bankingsystemapi.customer.dto.request.CustomerSearchRequestDTO;
import com.excellence.bankingsystemapi.model.Customer;
import com.excellence.bankingsystemapi.common.enums.CustomerStatus;
import com.excellence.bankingsystemapi.common.enums.Gender;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class CustomerSpecification {

    private CustomerSpecification() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated."
        );
    }

    /**
     * Builds the complete search specification.
     */
    public static Specification<Customer> search(
            CustomerSearchRequestDTO request
    ) {

        return Specification
                .where(hasKeyword(request.getKeyword()))
                .and(hasGender(request.getGender()))
                .and(hasStatus(request.getStatus()));
    }

    /**
     * Searches across multiple customer fields.
     */
    public static Specification<Customer> hasKeyword(
                    String keyword
    ) {

        return (root, query, cb) -> {

            if (!StringUtils.hasText(keyword)) {
                return cb.conjunction();
            }

            String search = "%" + keyword.trim().toLowerCase() + "%";

            return cb.or(

                    cb.like(
                            cb.lower(root.get("firstName")),
                            search
                    ),

                    cb.like(
                            cb.lower(root.get("lastName")),
                            search
                    ),

                    cb.like(
                            cb.lower(root.get("email")),
                            search
                    ),

                    cb.like(
                            cb.lower(root.get("phoneNumber")),
                            search
                    ),

                    cb.like(
                            cb.lower(root.get("customerNumber")),
                            search
                    )

            );

        };

    }

    /**
     * Filters by gender.
     */
    public static Specification<Customer> hasGender(
            Gender gender
    ) {

        return (root, query, cb) -> {

            if (gender == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("gender"),
                    gender
            );

        };

    }

    /**
     * Filters by customer status.
     */
    public static Specification<Customer> hasStatus(
            CustomerStatus status
    ) {

        return (root, query, cb) -> {

            if (status == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("customerStatus"),
                    status
            );

        };

    }

}

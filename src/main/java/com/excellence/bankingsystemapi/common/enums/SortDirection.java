package com.excellence.bankingsystemapi.common.enums;

import org.springframework.data.domain.Sort;

/**
 * Defines the allowed sorting directions for paginated requests.
 *
 * This enum is used to convert client input into Spring Data's Sort.Direction
 * while restricting values to a safe whitelist.
 */
public enum SortDirection {

    ASC,
    DESC;


    public Sort.Direction toSpringDirection() {
        return this == DESC ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

}
package com.excellence.bankingsystemapi.security.dto;


import java.util.Set;

public record AuthorizationDetails(

        Set<String> roles,

        Set<String> permissions) {
}


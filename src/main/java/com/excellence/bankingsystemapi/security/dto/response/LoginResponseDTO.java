package com.excellence.bankingsystemapi.security.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class LoginResponseDTO {
    private String email;

    private Set<String> roles;

    private Set<String> permissions;

    private String token;


}

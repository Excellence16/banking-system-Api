package com.excellence.bankingsystemapi.security.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterResponseDTO {

    private Long userId;

    private String email;

    private String message;
}

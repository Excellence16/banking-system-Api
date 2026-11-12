package com.excellence.bankingsystemapi.security.controller;


import com.excellence.bankingsystemapi.common.ApiResponse;
import com.excellence.bankingsystemapi.common.ApiResponseFactory;
import com.excellence.bankingsystemapi.security.dto.request.LoginRequestDTO;
import com.excellence.bankingsystemapi.security.dto.request.RegisterRequestDTO;
import com.excellence.bankingsystemapi.security.dto.response.LoginResponseDTO;
import com.excellence.bankingsystemapi.security.dto.response.RegisterResponseDTO;
import com.excellence.bankingsystemapi.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO requestDTO,
            HttpServletRequest request) {

        RegisterResponseDTO response = authenticationService.register(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseFactory.success("User registered successfully",
                        response,
                        HttpStatus.CREATED,
                        request));

    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO requestDTO,
            HttpServletRequest request) {

        LoginResponseDTO response = authenticationService.login(requestDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseFactory.success("Login Successful ",
                        response,
                        HttpStatus.OK,
                        request

                ));
    }

}

package com.excellence.bankingsystemapi.security.service;


import com.excellence.bankingsystemapi.security.dto.AuthorizationDetails;
import com.excellence.bankingsystemapi.security.dto.request.LoginRequestDTO;
import com.excellence.bankingsystemapi.security.dto.request.RegisterRequestDTO;
import com.excellence.bankingsystemapi.security.dto.response.LoginResponseDTO;
import com.excellence.bankingsystemapi.security.dto.response.RegisterResponseDTO;
import com.excellence.bankingsystemapi.security.model.Role;
import com.excellence.bankingsystemapi.security.model.User;
import com.excellence.bankingsystemapi.security.model.UserRole;
import com.excellence.bankingsystemapi.security.repository.RoleRepository;
import com.excellence.bankingsystemapi.security.repository.UserRepository;
import com.excellence.bankingsystemapi.security.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorizationService authorizationService;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO requestDTO) {

        String email = requestDTO.getEmail().trim().toLowerCase();


        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }


        Role customerRole = roleRepository
                .findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException(
                        "Customer role not found"));

        User user = User
                .builder()
                .email(email)
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .build();
        User savedUser = userRepository.save(user);

        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setRole(customerRole);

        userRoleRepository.save(userRole);
        savedUser.getUserRoles().add(userRole);

        return RegisterResponseDTO
                .builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .message("Registration successful")
                .build();


    }

    @Transactional
    public LoginResponseDTO login(LoginRequestDTO requestDTO) {

        String email = requestDTO.getEmail().trim().toLowerCase();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid " + "email or password"));


        if (!passwordEncoder.matches(requestDTO.getPassword(),
                user.getPassword())) {
            throw new UsernameNotFoundException("Password Do Not Match");
        }

        AuthorizationDetails authorizationDetails = authorizationService.getAuthorizationDetails(
                user);
        return LoginResponseDTO
                .builder()
                .email(user.getEmail())
                .roles(authorizationDetails.roles())
                .permissions(authorizationDetails.permissions())
                .token(null)
                .build();
    }


}

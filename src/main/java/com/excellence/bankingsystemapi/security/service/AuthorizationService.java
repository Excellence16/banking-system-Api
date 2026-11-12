package com.excellence.bankingsystemapi.security.service;

import com.excellence.bankingsystemapi.security.dto.AuthorizationDetails;
import com.excellence.bankingsystemapi.security.model.User;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorizationService {

    public AuthorizationDetails getAuthorizationDetails(User user) {

        Set<String> roles = user
                .getUserRoles()
                .stream()
                .map(userRole -> "ROLE_" + userRole.getRole().getName())
                .collect(Collectors.toUnmodifiableSet());

        Set<String> permissions = user
                .getUserRoles()
                .stream()
                .flatMap(userRole -> userRole
                        .getRole()
                        .getRolePermissions()
                        .stream())
                .map(rolePermission -> rolePermission.getPermission().getName())
                .collect(Collectors.toUnmodifiableSet());


        return new AuthorizationDetails(roles, permissions);


    }
}

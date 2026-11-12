package com.excellence.bankingsystemapi.security.jwt;


import com.excellence.bankingsystemapi.security.dto.AuthorizationDetails;
import com.excellence.bankingsystemapi.security.model.User;
import com.excellence.bankingsystemapi.security.repository.UserRepository;
import com.excellence.bankingsystemapi.security.service.AuthorizationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final AuthorizationService authorizationService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {


        /*
          Get Authorization header from incoming request.

          Expected format:

          Authorization: Bearer eyJhbGciOiJIUzI1...
        */
        final String authHeader = request.getHeader("Authorization");



        /*
          If there is no Authorization header,
          or it does not start with Bearer,

          we allow the request to continue.

          Public endpoints like:
          /auth/register
          /auth/login

          will reach their controllers normally.
        */
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            return;
        }



        /*
          Remove "Bearer " prefix.

          Example:

          Before:
          Bearer eyJhbGciOiJIUzI1...

          After:
          eyJhbGciOiJIUzI1...
        */
        String jwt = authHeader.substring(7);



        /*
          Extract user id from JWT subject.

          Remember our design:

          JWT:
          {
             "sub": "15",
             "email": "user@gmail.com"
          }

          sub = User.id
        */
        //Long userId = Long.parseLong(jwtService.extractSubject(jwt));

        Long userId = jwtService.extractUserId(jwt);



        /*
          Avoid creating authentication again
          if Spring Security already authenticated
          this request.
        */
        if (SecurityContextHolder.getContext().getAuthentication() == null) {



            /*
              Load user from database.

              Database remains the source of truth.
            */
            User user = userRepository.findById(userId).orElse(null);


            if (user != null && jwtService.isTokenValid(jwt, user)) {



                /*
                  Get roles and permissions from DB.

                  Example:

                  roles:
                  ROLE_CUSTOMER

                  permissions:
                  TRANSFER
                  VIEW_ACCOUNT
                */
                AuthorizationDetails authorizationDetails = authorizationService.getAuthorizationDetails(
                        user);



                /*
                  Spring Security does not understand:

                  Set<String>

                  It understands:

                  GrantedAuthority


                  So we convert:

                  ROLE_CUSTOMER
                  TRANSFER

                  into:

                  SimpleGrantedAuthority("ROLE_CUSTOMER")
                  SimpleGrantedAuthority("TRANSFER")
                */
                Collection<GrantedAuthority> authorities = Stream
                        .concat(authorizationDetails.roles().stream(),

                                authorizationDetails.permissions().stream())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());



                /*
                  Create authenticated object.

                  Principal:
                  user

                  Credentials:
                  null

                  Authorities:
                  roles + permissions
                */
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        authorities);



                /*
                  Store authentication inside SecurityContext.

                  From this point:

                  Spring Security knows:

                  "This request belongs to this user."
                */
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }
        }



        /*
          Continue the filter chain.

          Eventually the request reaches:

          Controller → Service → Response
        */
        filterChain.doFilter(request, response);
    }
}

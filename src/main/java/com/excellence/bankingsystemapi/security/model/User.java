package com.excellence.bankingsystemapi.security.model;


import com.excellence.bankingsystemapi.model.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean accountNonExpired = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean accountNonLocked = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean credentialsNonExpired = true;

    @OneToOne(mappedBy = "user")
    private Customer customer;


    @OneToOne(mappedBy = "user")
    private Staff staff;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<UserRole> userRoles = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        for (UserRole userRole : userRoles) {
            Role role = userRole.getRole();

            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));


            for (RolePermission rolePermission : role.getRolePermissions()) {
                authorities.add(new SimpleGrantedAuthority(rolePermission
                        .getPermission()
                        .getName()));
            }


        }
        return authorities;
    }
    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    // return userRoles
    // .stream()
    //.flatMap(userRole -> Stream.concat(Stream.of(new
    // SimpleGrantedAuthority(
    //  "ROLE_" + userRole.getRole().getName())),
    //userRole
    //.getRole()
    //.getRolePermissions()
    //.stream()
    // .map(rolePermission -> new
    // SimpleGrantedAuthority(
    //   rolePermission
    //  .getPermission()
    //  .getName()))))
    //.collect(Collectors.toSet());

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

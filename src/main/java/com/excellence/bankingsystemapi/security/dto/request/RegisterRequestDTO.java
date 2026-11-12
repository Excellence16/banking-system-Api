package com.excellence.bankingsystemapi.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterRequestDTO {


    @NotBlank(message = "Email is required")
    @Email(message = " please provide a valid Email address")
    private String email;

    @NotBlank(message = "password is required ")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&()#^+=_" + ".-])[A-Za-z\\d@$!%*?&()#^+=_.-]{8,100}$", message = " password " + "must be between 8 and 100 " + "characters long and contain at " + "least one uppercase letter,one lowercase letter, one number, and" + " one special character.")
    
    private String password;
}

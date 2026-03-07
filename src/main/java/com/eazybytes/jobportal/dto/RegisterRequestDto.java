package com.eazybytes.jobportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {

    @NotBlank(message="User name is required")
    @Size(min=5, max = 30, message = "The length of the name should be between 5 and 30")
    private String name;

    @Email(message = "Enter a valid email address")
    @NotBlank(message = "Email should not be blank")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max=30, message ="Password should be between 8 and 30 characters")
    private String password;

    @NotBlank(message = "Mobile number should be required")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number should be 10 digits")
    private String mobileNumber;
}

package com.eazybytes.jobportal.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;

public record ContactRequestDto(

        @NotBlank(message = "Name should not be null")
        @Size(min = 3, max=30, message = "Name should be between 3 and 30 characters")
        String name,

        @NotBlank(message = "Email should not be null")
        @Email(message = "Invalid Email")
        String email,

        @NotBlank(message = "User Type should not be null")
        @Pattern(regexp = "Job Seeker|Employer|Other|", message = "UserType should be of Job Seeker or Employer or Other")
        String userType,

        @NotBlank(message = "Subject should not be null")
        @Size(min = 5, max=30, message = "Subject should be between 5 and 30 characters")
        String subject,

        @NotBlank(message = "Message should not be null")
        @Size(min = 10, max=500, message = "Name should be between 10 and 500 characters")
        String message

) implements Serializable {
}

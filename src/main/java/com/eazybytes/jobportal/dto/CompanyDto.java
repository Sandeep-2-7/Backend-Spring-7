package com.eazybytes.jobportal.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
//@RequiredArgsConstructor
public class CompanyDto {

    private final Long id;

    @NotBlank(message = "Name can not be empty")
    private final String name;

    @NotBlank(message = "Logo can not be empty")
    private final String logo;

    @NotBlank(message = "Industry can not be empty")
    private final String industry;

    @NotBlank(message = "Size can not be empty")
    private final String size;

    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5.0")
    private final BigDecimal rating;

    @NotBlank(message = "Locations can not be empty")
    private final String locations;

    @Min(value = 1900, message = "Founded year should 1900 or greater")
    private final Integer founded;

    @NotBlank(message = "Description can not be empty")
    private final String description;

    @Min(value = 1, message = "Employees must be equal or greater than 1")
    private final Integer employees;

    @NotBlank(message = "Website can not be empty")
    private final String website;

    private final Instant createdAt;

    private final List<JobDto> jobs;
}

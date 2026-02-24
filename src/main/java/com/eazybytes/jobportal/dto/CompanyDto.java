package com.eazybytes.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@AllArgsConstructor
public class CompanyDto {

    private final Long id;
    private final String name;
    private final String logo;
    private final String industry;
    private final String size;
    private final BigDecimal rating;
    private final String locations;
    private final Integer founded;
    private final String description;
    private final Integer employees;
    private final String website;
    private final Instant createdAt;
}

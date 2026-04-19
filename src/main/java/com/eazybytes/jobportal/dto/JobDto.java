package com.eazybytes.jobportal.dto;

import com.eazybytes.jobportal.entity.Company;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class JobDto implements Serializable
{
    private final Long id;

    @NotBlank(message = "Job title required")
    private final String title;

    private final Long companyId;
    private final String companyName;
    private final String companyLogo;

    @NotBlank(message = "Job Location is required")
    private final String location;

    @NotBlank(message = "WorkType is required")
    private final String workType;

    @NotBlank(message = "JobType is required")
    private final String jobType;

    @NotBlank(message = "Category is required")
    private final String category;

    @NotBlank(message = "Experience Level is required")
    private final String experienceLevel;

    @NotNull(message = "Minimum salary is required")
    @DecimalMin(value = "0.0", message = "Minimum salary must be positive")
    private final BigDecimal salaryMin;

    @NotNull(message = "Maximum salary is required")
    @DecimalMin(value = "0.0", message = "Maximum salary must be positive")
    private final BigDecimal salaryMax;

    @NotBlank(message = "Salary Currency is required")
    private final String salaryCurrency;

    @NotBlank(message = "Salary Period is required")
    private final String salaryPeriod;

    @NotBlank(message = "Description is required")
    private final String description;
    private final String requirements;
    private final String benefits;
    private final Instant postedDate;
    private final Instant applicationDeadline;
    private final Integer applicationsCount;
    private final Boolean featured;
    private final Boolean urgent;
    private final Boolean remote;
    private final String status;
}

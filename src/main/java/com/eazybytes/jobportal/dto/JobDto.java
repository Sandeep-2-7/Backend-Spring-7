package com.eazybytes.jobportal.dto;

import com.eazybytes.jobportal.entity.Company;
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
    private final String title;
    private final Long companyId;
    private final String companyName;
    private final String companyLogo;
    private final String location;
    private final String workType;
    private final String jobType;
    private final String category;
    private final String experienceLevel;
    private final BigDecimal salaryMin;
    private final BigDecimal salaryMax;
    private final String salaryCurrency;
    private final String salaryPeriod;
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

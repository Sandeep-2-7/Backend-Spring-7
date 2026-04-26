package com.eazybytes.jobportal.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class JobApplicationDto {
    private final Long id;
    private final Long userId;
    private final String userName;
    private final String userEmail;
    private final String userMobileNumber;
    private final ProfileDto userProfile;
    private final JobDto job;
    private final Instant appliedAt;
    private final String status;
    private final String coverLetter;
    private final String notes;
}

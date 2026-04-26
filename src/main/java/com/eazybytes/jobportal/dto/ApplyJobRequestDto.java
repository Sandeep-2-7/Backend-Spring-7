package com.eazybytes.jobportal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApplyJobRequestDto{

    @NotNull(message = "Job Id is required")
    private final Long jobId;
    private final String cover;
}

package com.eazybytes.jobportal.job.service;

import com.eazybytes.jobportal.dto.JobDto;
import com.eazybytes.jobportal.entity.Job;
import jakarta.validation.Valid;

import java.util.List;

public interface JobService {

    List<JobDto> findUser(String email);

    JobDto updateJobStatus(String email, Long id, String status);

    JobDto createJob(String email, @Valid JobDto jobDto);
}

package com.eazybytes.jobportal.job.service.Impl;

import com.eazybytes.jobportal.dto.JobDto;
import com.eazybytes.jobportal.entity.Job;
import com.eazybytes.jobportal.entity.JobPortalUser;
import com.eazybytes.jobportal.job.service.JobService;
import com.eazybytes.jobportal.repository.JobPortalUserRepository;
import com.eazybytes.jobportal.repository.JobRepository;
import com.eazybytes.jobportal.utility.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.util.BeanUtil;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobPortalUserRepository jobPortalUserRepository;
    private final JobRepository jobRepository;

    @Override
    public List<JobDto> findUser(String email) {
        JobPortalUser user = jobPortalUserRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getCompanyId() == null)
            throw new  RuntimeException("Company not assigned..");

        List<Job> jobs = user.getCompanyId().getJobs();
        return jobs.stream().map(job -> ApplicationUtility.transformJobToDto(job)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobDto updateJobStatus(String email, Long id, String status) {
     if(!status.equals("ACTIVE") && !status.equals("CLOSED") && !status.equals("DRAFT"))
         throw new  RuntimeException("Invalid Status - Status should be Active or Closed or Drafted");

        JobPortalUser employer =  jobPortalUserRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));

        if (employer.getCompanyId() == null) {
            throw new RuntimeException("Employer does not have a company assigned");
        }

        Job job = employer.getCompanyId().getJobs().stream().filter(j -> j.getId().equals(id)).findFirst()
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus(status);
        return ApplicationUtility.transformJobToDto(job);
    }

    @Override
    @Transactional
    public JobDto createJob(String email, JobDto jobDto) {
        JobPortalUser user = jobPortalUserRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));

        if (user.getCompanyId() == null)
            throw new RuntimeException("Company not assigned..");

        Job job = transformJobDtoToJob(jobDto);
        job.setCompany(user.getCompanyId());
        job.setPostedDate(Instant.now());
        job.setStatus("DRAFT");
        job.setApplicationsCount(0);
        Job j = jobRepository.save(job);
        return ApplicationUtility.transformJobToDto(j);
    }

    private Job transformJobDtoToJob(JobDto jobDto) {
        Job job = new Job();
        BeanUtils.copyProperties(jobDto, job);
        return job;
    }
}

package com.eazybytes.jobportal.job.service.Impl;

import com.eazybytes.jobportal.dto.JobApplicationDto;
import com.eazybytes.jobportal.dto.JobDto;
import com.eazybytes.jobportal.entity.Job;
import com.eazybytes.jobportal.entity.JobApplication;
import com.eazybytes.jobportal.entity.JobPortalUser;
import com.eazybytes.jobportal.job.service.JobService;
import com.eazybytes.jobportal.repository.JobApplicationRepository;
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
    private final JobApplicationRepository jobApplicationRepository;

    @Override
    public List<JobDto> findUser(String email) {
        JobPortalUser user = jobPortalUserRepository.findJobPortalUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getCompany() == null)
            throw new  RuntimeException("Company not assigned..");

        List<Job> jobs = user.getCompany().getJobs();
        return jobs.stream().map(job -> ApplicationUtility.transformJobToDto(job)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobDto updateJobStatus(String email, Long id, String status) {
     if(!status.equals("ACTIVE") && !status.equals("CLOSED") && !status.equals("DRAFT"))
         throw new  RuntimeException("Invalid Status - Status should be Active or Closed or Drafted");

        JobPortalUser employer =  jobPortalUserRepository.findJobPortalUserByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));

        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned");
        }

        Job job = employer.getCompany().getJobs().stream().filter(j -> j.getId().equals(id)).findFirst()
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus(status);
        return ApplicationUtility.transformJobToDto(job);
    }

    @Override
    @Transactional
    public JobDto createJob(String email, JobDto jobDto) {
        JobPortalUser user = jobPortalUserRepository.findJobPortalUserByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));

        if (user.getCompany() == null)
            throw new RuntimeException("Company not assigned..");

        Job job = transformJobDtoToJob(jobDto);
        job.setCompany(user.getCompany());
        job.setPostedDate(Instant.now());
        job.setStatus("DRAFT");
        job.setApplicationsCount(0);
        Job j = jobRepository.save(job);
        return ApplicationUtility.transformJobToDto(j);
    }

    @Override
    public List<JobApplicationDto> getApplicationByJobId(Long jobId) {
        List<JobApplication> application = jobApplicationRepository.findByJobIdOrderByAppliedAtAsc(jobId);
        return application.stream().map(ApplicationUtility::mapToJobApplicationDto).toList();
    }

    private Job transformJobDtoToJob(JobDto jobDto) {
        Job job = new Job();
        BeanUtils.copyProperties(jobDto, job);
        return job;
    }
}

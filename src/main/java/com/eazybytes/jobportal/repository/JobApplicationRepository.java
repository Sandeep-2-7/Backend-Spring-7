package com.eazybytes.jobportal.repository;

import com.eazybytes.jobportal.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {
    boolean existsByJobIdAndUserId(Long jobId, Long userId);
    void deleteByJobIdAndUserId(Long jobId, Long userId);
    List<JobApplication> findByJobIdOrderByAppliedAtAsc(Long jobId);
}

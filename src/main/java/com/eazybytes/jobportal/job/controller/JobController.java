package com.eazybytes.jobportal.job.controller;


import com.eazybytes.jobportal.dto.JobDto;
import com.eazybytes.jobportal.job.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping(value = "/employer", version = "1.0")
    public ResponseEntity<List<JobDto>> getJobsForUser(Authentication authentication) {
        String email = authentication.getName();
        List<JobDto> jobs = jobService.findUser(email);
        return ResponseEntity.status(HttpStatus.OK).body(jobs);
    }

    @PatchMapping("/{jobId}/status/employer")
    public ResponseEntity<?> updateJobStatus(@PathVariable Long jobId, @RequestBody Map<String, String> requestBody, Authentication authentication) {
        String email = authentication.getName();
        String status = requestBody.get("status");

        if(status == null || status.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status should not be empty or null");
        }

        JobDto job = jobService.updateJobStatus(email,jobId,status.toUpperCase());
        return ResponseEntity.status(HttpStatus.OK).body(job);
    }

    @PostMapping(value = "/employer", version = "1.0")
    public ResponseEntity<JobDto> createJob(@RequestBody @Valid JobDto jobDto, Authentication authentication) {
        String email = authentication.getName();
        JobDto dto = jobService.createJob(email, jobDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}

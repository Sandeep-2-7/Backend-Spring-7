package com.eazybytes.jobportal.repository;

import com.eazybytes.jobportal.entity.JobPortalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobPortalUserRepository extends JpaRepository<JobPortalUser,Long> {

    Optional<JobPortalUser> readJobPortalUserByEmailOrMobileNumber(String email, String mobileNumber);
}

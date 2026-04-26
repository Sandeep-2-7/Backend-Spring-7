package com.eazybytes.jobportal.user.Service;

import com.eazybytes.jobportal.dto.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<UserDto> searchByEmail(String email);

    UserDto updateRole(Long userId);

    UserDto updateCompnaytoUser(Long userId, Long companyId);

    ProfileDto createOrUpdateCompany(String email, String profileJson, MultipartFile profilePicture, MultipartFile resume);

    ProfileDto getProfile(String email);

    ProfileDto getProfilePicture(String email);

    ProfileDto getResume(String email);

    JobDto saveJob(String email, Long jobId);

    void unsaveJob(String email, Long jobId);

    List<JobDto> getAllSavedJobs(String email);

    JobApplicationDto applyJob(String email, @Valid ApplyJobRequestDto applyJobRequestDto);

    void withdrawJob(@Valid Long jobId, String email);

    List<JobApplicationDto> getAllJobs(String email);
}

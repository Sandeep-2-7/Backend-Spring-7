package com.eazybytes.jobportal.user.Service;

import com.eazybytes.jobportal.dto.ProfileDto;
import com.eazybytes.jobportal.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {

    Optional<UserDto> searchByEmail(String email);

    UserDto updateRole(Long userId);

    UserDto updateCompnaytoUser(Long userId, Long companyId);

    ProfileDto createOrUpdateCompany(String email, String profileJson, MultipartFile profilePicture, MultipartFile resume);

    ProfileDto getProfile(String email);

    ProfileDto getProfilePicture(String email);

    ProfileDto getResume(String email);
}

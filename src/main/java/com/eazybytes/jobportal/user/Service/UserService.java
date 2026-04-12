package com.eazybytes.jobportal.user.Service;

import com.eazybytes.jobportal.dto.UserDto;

import java.util.Optional;

public interface UserService {

    Optional<UserDto> searchByEmail(String email);

    UserDto updateRole(Long userId);

    UserDto updateCompnaytoUser(Long userId, Long companyId);
}

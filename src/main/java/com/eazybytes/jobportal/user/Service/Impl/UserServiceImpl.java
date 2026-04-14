package com.eazybytes.jobportal.user.Service.Impl;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.dto.UserDto;
import com.eazybytes.jobportal.entity.Company;
import com.eazybytes.jobportal.entity.JobPortalUser;
import com.eazybytes.jobportal.entity.Roles;
import com.eazybytes.jobportal.repository.CompanyRepository;
import com.eazybytes.jobportal.repository.JobPortalUserRepository;
import com.eazybytes.jobportal.repository.RoleRepository;
import com.eazybytes.jobportal.user.Service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final JobPortalUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;

    @Override
    public Optional<UserDto> searchByEmail(String email) {
        Optional<JobPortalUser> user = userRepository.findByEmail(email);
        return user.map(this::maptoUserDto);
    }

    @Override
    @Transactional
    public UserDto updateRole(Long userId) {
        JobPortalUser user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No user Id found"));

        if(ApplicationConstants.ROLE_EMPLOYEER.equals(user.getRole().getName()))
            return maptoUserDto(user);

        if(ApplicationConstants.ROLE_ADMIN.equals(user.getRole().getName()))
            throw new RuntimeException("The user is already an admin");

        Roles role = roleRepository.findRolesByName(ApplicationConstants.ROLE_EMPLOYEER).orElseThrow(() -> new RuntimeException("No Employeer role found"));
        System.out.println(role.getName());
        user.setRole(role);

        return maptoUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateCompnaytoUser(Long userId, Long companyId) {
        JobPortalUser user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user found with id " + userId));

        Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("No company found with id " + companyId));

        if(!user.getRole().getName().equals(ApplicationConstants.ROLE_EMPLOYEER))
            throw new RuntimeException("User must be an Employeer tog et assigned to a company");

        user.setCompanyId(company);

        return maptoUserDto(user);
    }


    private UserDto maptoUserDto(JobPortalUser user){
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user,userDto);
        userDto.setUserId(user.getId());
        userDto.setRole(user.getRole()!=null ? user.getRole().getName() : null);
        userDto.setCompanyId(user.getCompanyId()!=null ? user.getCompanyId().getId() : null);
        userDto.setCompanyName(user.getCompanyId()!=null ? user.getCompanyId().getName() : null);

        return userDto;
    }
}

package com.eazybytes.jobportal.user.Service.Impl;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.dto.*;
import com.eazybytes.jobportal.entity.*;
import com.eazybytes.jobportal.repository.*;
import com.eazybytes.jobportal.user.Service.UserService;
import com.eazybytes.jobportal.utility.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final JobPortalUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final ProfileRepository profileRepository;
    private final JobRepository  jobRepository;
    private final JobApplicationRepository jobApplicationRepository;

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

    @Override
    @Transactional
    public ProfileDto createOrUpdateCompany(String email, String profileJson, MultipartFile profilePicture, MultipartFile resume) {
        JobPortalUser user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("No user Id found"));
        Profile profile = user.getProfile();
        if(profile==null)
        {
            profile = new Profile();
            profile.setUser(user);
        }
        ObjectMapper mapper = new ObjectMapper();
        ProfileDto profileDto = mapper.readValue(profileJson, ProfileDto.class);
        Profile savedProfile = profileRepository.save(maptoProfile(profile,profileDto, profilePicture, resume));
        return maptoProfileDto(savedProfile,false);
    }

    @Override
    public ProfileDto getProfile(String email) {
        JobPortalUser user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Profile profile = user.getProfile();

        if(profile==null){
            return null;
        }

        return maptoProfileDto(profile,false);
    }

    @Override
    public ProfileDto getProfilePicture(String email) {
        JobPortalUser user =  userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Profile profile = user.getProfile();

        if(profile.getProfilePicture()==null){
            return null;
        }
        return maptoProfileDto(profile,true);
    }

    @Override
    public ProfileDto getResume(String email) {
        JobPortalUser user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Profile profile = user.getProfile();
       if(profile == null)
           return null;

       return maptoProfileDto(profile,true);
    }

    @Override
    @Transactional
    public JobDto saveJob(String email, Long jobId) {
        JobPortalUser user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NoSuchElementException("No job found with id " + jobId));

        user.getSavedJobs().add(job);
        return ApplicationUtility.transformJobToDto(job);
    }

    @Override
    @Transactional
    public void unsaveJob(String email, Long jobId) {
        JobPortalUser user =  userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NoSuchElementException("No job found with id "));

        user.getSavedJobs().remove(job);
    }

    @Override
    public List<JobDto> getAllSavedJobs(String email) {
        JobPortalUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return user.getSavedJobs().stream().map(job -> ApplicationUtility.transformJobToDto(job))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobApplicationDto applyJob(String email, ApplyJobRequestDto applyJobRequestDto) {
        JobPortalUser user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Long jobId = applyJobRequestDto.getJobId();
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NoSuchElementException("No job found with id " + jobId));
        if(jobApplicationRepository.existsByJobIdAndUserId(jobId, user.getId()))
            throw new RuntimeException("Already applied for this job");

        JobApplication jobApplication = new JobApplication();
        jobApplication.setUser(user);
        jobApplication.setJob(job);
        jobApplication.setAppliedAt(Instant.now());
        jobApplication.setCoverLetter(applyJobRequestDto.getCover());
        jobApplication.setStatus("PENDING");
        jobApplicationRepository.save(jobApplication);

        job.setApplicationsCount(job.getApplicationsCount()!=null ?  job.getApplicationsCount()+1 : 0);
        return mapToJobApplicationDto(jobApplication);
    }

    @Override
    @Transactional
    public void withdrawJob(Long jobId, String email) {
        JobPortalUser user =  userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NoSuchElementException("No job found with id "));

        if(!jobApplicationRepository.existsByJobIdAndUserId(jobId, user.getId()))
            throw new RuntimeException("You didn't applied for this job");

        jobApplicationRepository.deleteByJobIdAndUserId(jobId, user.getId());

        if(job.getApplicationsCount()!=null &&  job.getApplicationsCount()>0){
            job.setApplicationsCount(job.getApplicationsCount()-1);
        }

    }

    @Override
    public List<JobApplicationDto> getAllJobs(String email) {
        JobPortalUser user =  userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getJobApplications().stream().map(this::mapToJobApplicationDto).collect(Collectors.toList());
    }

    private JobApplicationDto mapToJobApplicationDto(JobApplication jobApplication) {

        Profile profile = jobApplication.getUser().getProfile();
        ProfileDto profileDto=maptoProfileDto(profile,true);
        Job job = jobApplication.getJob();
        JobDto jobDto = ApplicationUtility.transformJobToDto(job);
        JobApplicationDto jobApplicationDto = new JobApplicationDto(jobApplication.getId(),
                jobApplication.getUser().getId(), jobApplication.getUser().getName(),
                jobApplication.getUser().getName(),jobApplication.getUser().getMobileNumber(),
                profileDto, jobDto, jobApplication.getAppliedAt(),
                jobApplication.getStatus(), jobApplication.getCoverLetter(), jobApplication.getNotes());
        return jobApplicationDto;
    }

    private ProfileDto maptoProfileDto(Profile profile, boolean includeBinaryData) {
        ProfileDto dto;
        if (includeBinaryData) {
            dto = new ProfileDto(profile.getId(), profile.getUser().getId(),
                    profile.getJobTitle(), profile.getLocation(), profile.getExperienceLevel(),
                    profile.getProfessionalBio(), profile.getPortfolioWebsite(), profile.getProfilePicture(),
                    profile.getProfilePictureName(), profile.getProfilePictureType(), profile.getResume(),
                    profile.getResumeName(), profile.getResumeType(), profile.getCreatedAt(), profile.getUpdatedAt()
            );
        } else {
            dto = new ProfileDto(profile.getId(), profile.getUser().getId(),
                    profile.getJobTitle(), profile.getLocation(), profile.getExperienceLevel(),
                    profile.getProfessionalBio(), profile.getPortfolioWebsite(), null,
                    profile.getProfilePictureName(), profile.getProfilePictureType(), null,
                    profile.getResumeName(), profile.getResumeType(), profile.getCreatedAt(), profile.getUpdatedAt());
        }
        return dto;
    }

    private Profile maptoProfile(Profile profile, ProfileDto profileDto, MultipartFile profilePicture, MultipartFile resume) {
        profile.setJobTitle(profileDto.jobTitle());
        profile.setLocation(profileDto.location());
        profile.setExperienceLevel(profileDto.experienceLevel());
        profile.setPortfolioWebsite(profileDto.portfolioWebsite());
        profile.setProfessionalBio(profileDto.professionalBio());

        if(profilePicture!=null && !profilePicture.isEmpty()){
            try{
                profile.setProfilePicture(profilePicture.getBytes());
                profile.setProfilePictureName(profilePicture.getOriginalFilename());
                profile.setProfilePictureType(profilePicture.getContentType());
            }
            catch (IOException e){
                    throw new RuntimeException("Failed to set profile picture",e);
            }
        }

        if(resume !=null && !resume.isEmpty()){
            try{
                profile.setResume(resume.getBytes());
                profile.setResumeName(resume.getOriginalFilename());
                profile.setResumeType(resume.getContentType());
            }
            catch (IOException e){
                throw new RuntimeException("Failed to set profile resume",e);
            }
        }
        return  profile;
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

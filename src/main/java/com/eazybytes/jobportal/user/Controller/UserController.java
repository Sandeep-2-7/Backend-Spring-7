package com.eazybytes.jobportal.user.Controller;

import com.eazybytes.jobportal.dto.ProfileDto;
import com.eazybytes.jobportal.dto.UserDto;
import com.eazybytes.jobportal.user.Service.Impl.UserServiceImpl;
import com.eazybytes.jobportal.user.Service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/search/admin", version = "1.0")
    public ResponseEntity<?> searchByEmail(@RequestParam String email){
        Optional<UserDto> user = userService.searchByEmail(email);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }

    @PatchMapping("/{userId}/role/employer/admin")
    public ResponseEntity<?> updateRole(@PathVariable Long userId){
        UserDto user =userService.updateRole(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping("/{userId}/company/{companyId}/admin")
    public ResponseEntity<?> updateCompanytoUser(@PathVariable Long userId, @PathVariable Long companyId){
        UserDto user = userService.updateCompnaytoUser(userId, companyId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping(value = "/profile/jobseeker", version = "1.0", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createOrUpdateCompany(@RequestPart(value = "profile") String profileJson,
                                                   @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
                                                   @RequestPart(value = "resume", required = false) MultipartFile resume,
                                                   Authentication authentication){
        String email =  authentication.getName();
        ProfileDto dto = userService.createOrUpdateCompany(email, profileJson, profilePicture, resume);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping(value = "/profile/jobseeker", version = "1.0")
    public ResponseEntity<ProfileDto> getProfile(Authentication authentication){
        String email = authentication.getName();
        ProfileDto profileDto = userService.getProfile(email);
        return ResponseEntity.status(HttpStatus.OK).body(profileDto);
    }

    @GetMapping("/profile/picture/jobseeker")
    public ResponseEntity<byte[]> getProfilePicture(Authentication authentication){
        String email = authentication.getName();
        ProfileDto profileDto =  userService.getProfilePicture(email);

       byte[] picture = profileDto.profilePicture();
       if(picture.length == 0 || picture==null)
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(profileDto.profilePictureType()));
        headers.setContentLength(profileDto.profilePicture().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(picture);
    }

    @GetMapping("/profile/resume/jobseeker")
    public ResponseEntity<byte[]> getResume(Authentication authentication){
        String email = authentication.getName();
        ProfileDto profileDto = userService.getResume(email);

        byte[] resume = profileDto.resume();
        if(resume.length == 0 ||resume==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(profileDto.resumeType()));
        headers.setContentLength(profileDto.resume().length);
        headers.setContentDispositionFormData("attachment", profileDto.resumeName());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resume);
    }
}

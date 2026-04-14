package com.eazybytes.jobportal.user.Controller;

import com.eazybytes.jobportal.dto.UserDto;
import com.eazybytes.jobportal.user.Service.Impl.UserServiceImpl;
import com.eazybytes.jobportal.user.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

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
}

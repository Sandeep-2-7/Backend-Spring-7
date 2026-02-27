package com.eazybytes.jobportal.contact.controller;

import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.dto.ContactRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final IContactService contactService;
    @PostMapping(version = "1.0")
    public ResponseEntity<String> saveContactMsg(@RequestBody @Valid ContactRequestDto contactReqDto){
        boolean b = contactService.saveContact(contactReqDto);
        if(b)
            return ResponseEntity.ok().body("Saved Successfully");

        return ResponseEntity.internalServerError().body("Request failed");
    }

    @GetMapping(value = "/openContacts", version = "1.0")
    public ResponseEntity<String>  fetchOpenContacts(@RequestParam
                                                         @Validated @NotBlank(message = "Name should not be blank")
                                                         String name){
        return ResponseEntity.ok().body("Hi");
    }
}

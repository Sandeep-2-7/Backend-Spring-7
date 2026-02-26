package com.eazybytes.jobportal.contact.controller;

import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.dto.ContactRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final IContactService contactService;
    @PostMapping(version = "1.0")
    public ResponseEntity<String> saveContactMsg(@RequestBody ContactRequestDto contactReqDto){
        boolean b = contactService.saveContact(contactReqDto);
        if(b)
            return ResponseEntity.ok().body("Saved Successfully");

        return ResponseEntity.internalServerError().body("Request failed");
    }
}

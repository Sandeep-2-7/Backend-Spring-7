package com.eazybytes.jobportal.contact.controller;

import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.dto.ContactRequestDto;
import com.eazybytes.jobportal.dto.ContactResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final IContactService contactService;
    @PostMapping(path = "/public", version = "1.0")
    public ResponseEntity<String> saveContactMsg(@RequestBody @Valid ContactRequestDto contactReqDto){
        boolean b = contactService.saveContact(contactReqDto);
        if(b)
            return ResponseEntity.ok().body("Saved Successfully");

        return ResponseEntity.internalServerError().body("Request failed");
    }

//    @GetMapping(value = "/openContacts", version = "1.0")
//    public ResponseEntity<String>  fetchOpenContacts(@RequestParam
//                                                         @Validated @NotBlank(message = "Name should not be blank")
//                                                         String name){
//        return ResponseEntity.ok().body("Hi");
//    }


    @GetMapping("/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchOpenContactMsgs(){
        List<ContactResponseDto> openContactMsgs = contactService.fetchOpenContacts();
        return ResponseEntity.status(HttpStatus.OK).body(openContactMsgs);
    }

    @GetMapping("/sort/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchOpenContactMsgsWithSort(@RequestParam(defaultValue = "createdAt") String sort,
                                                                                 @RequestParam(defaultValue = "Asc") String order){
        List<ContactResponseDto> openContactMsgs = contactService.fetchOpenContactsWithSort(sort, order);
        return ResponseEntity.status(HttpStatus.OK).body(openContactMsgs);
    }
}

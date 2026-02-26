package com.eazybytes.jobportal.contact.service.impl;

import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.dto.ContactRequestDto;
import com.eazybytes.jobportal.entity.Contact;
import com.eazybytes.jobportal.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;

    @Override
    public boolean saveContact(ContactRequestDto contactReqDto) {
        Contact c= transformToDto(contactReqDto);
        Contact c2 = contactRepository.save(c);
        if(c2!=null  && c2.getId()!=null)
            return true;
        return false;
    }

    public Contact  transformToDto(ContactRequestDto contactReqDto){
        Contact c = new Contact();
        BeanUtils.copyProperties(contactReqDto,c);
        c.setCreatedAt(Instant.now());
        c.setCreatedBy("SYSTEM");
        c.setStatus("NEW");
        return c;
    }
}

package com.eazybytes.jobportal.contact.service.impl;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.dto.ContactRequestDto;
import com.eazybytes.jobportal.dto.ContactResponseDto;
import com.eazybytes.jobportal.entity.Contact;
import com.eazybytes.jobportal.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;

    @Override
    public boolean saveContact(ContactRequestDto contactReqDto) {
        Contact c= transformToEntity(contactReqDto);
        Contact c2 = contactRepository.save(c);
        if(c2!=null  && c2.getId()!=null)
            return true;
        return false;
    }

    @Override
    public List<ContactResponseDto> fetchOpenContacts() {
        List<Contact>  contacts =  contactRepository.findContactByStatusOrderByCreatedAtAsc(ApplicationConstants.NEW_MSG);
        return contacts.stream().map(this::transformtoDto).toList();

    }

    @Override
    public List<ContactResponseDto> fetchOpenContactsWithSort(String sort, String order) {
        Sort by = order.equalsIgnoreCase("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        List<Contact>  contacts = contactRepository.findContactByStatus(ApplicationConstants.NEW_MSG,by);
        List<ContactResponseDto> contactDto = contacts.stream().map(this::transformtoDto).toList();
        return contactDto;
    }

    public ContactResponseDto transformtoDto(Contact contact){
        ContactResponseDto contactDto = new ContactResponseDto(contact.getId(), contact.getName(),
                contact.getEmail(), contact.getUserType(), contact.getSubject(), contact.getMessage(),
                contact.getStatus(), contact.getCreatedAt());

        return contactDto;
    }

    public Contact  transformToEntity(ContactRequestDto contactReqDto){
        Contact c = new Contact();
        BeanUtils.copyProperties(contactReqDto,c);
        c.setStatus(ApplicationConstants.NEW_MSG);
        return c;
    }
}

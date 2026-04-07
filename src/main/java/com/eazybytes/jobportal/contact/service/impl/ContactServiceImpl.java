package com.eazybytes.jobportal.contact.service.impl;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.dto.ContactRequestDto;
import com.eazybytes.jobportal.dto.ContactResponseDto;
import com.eazybytes.jobportal.entity.Contact;
import com.eazybytes.jobportal.repository.ContactRepository;
import com.eazybytes.jobportal.utility.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;

    @Override
    @Transactional
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

    @Override
    public Page<ContactResponseDto> fetchOpenContactsWithPaginationAndSort(int pageCount, int pageSize, String sortBy, String sortDir)
    {
        Sort by = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        PageRequest request = PageRequest.of(pageCount, pageSize,by);
        Page<Contact> contacts = contactRepository.findContactByStatus(ApplicationConstants.NEW_MSG,request);
        return contacts.map(this::transformtoDto);
    }

    @Override
    @Transactional
    public boolean closeContactMsg(Long id, String status) {
        int result = contactRepository.updateStatusById(status,id, ApplicationUtility.getLoggedInUser());
        return result>0;
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

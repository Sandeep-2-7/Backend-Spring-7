package com.eazybytes.jobportal.contact.service;

import com.eazybytes.jobportal.dto.ContactRequestDto;
import com.eazybytes.jobportal.dto.ContactResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IContactService {
    boolean saveContact(ContactRequestDto contactReqDto);
    List<ContactResponseDto> fetchOpenContacts ();

    List<ContactResponseDto> fetchOpenContactsWithSort(String sort, String order);

    Page<ContactResponseDto> fetchOpenContactsWithPaginationAndSort (int page, int pageSize, String sort, String order);

    boolean closeContactMsg(Long id, String status);
}

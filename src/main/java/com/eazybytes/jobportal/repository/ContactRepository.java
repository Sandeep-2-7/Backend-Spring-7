package com.eazybytes.jobportal.repository;

import com.eazybytes.jobportal.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findContactByStatus (String status, Sort sort);

    List<Contact> findContactByStatusOrderByCreatedAtAsc (String status);

    Page<Contact> findContactByStatus (String status, Pageable pageable);
}

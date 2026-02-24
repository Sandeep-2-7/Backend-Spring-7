package com.eazybytes.jobportal.service;

import com.eazybytes.jobportal.entity.Company;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ICompanyService {
    List<Company> getAllCompanies();
}

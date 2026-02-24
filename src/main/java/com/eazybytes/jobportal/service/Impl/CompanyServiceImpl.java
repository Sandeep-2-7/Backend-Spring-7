package com.eazybytes.jobportal.service.Impl;

import com.eazybytes.jobportal.entity.Company;
import com.eazybytes.jobportal.repository.CompanyRepository;
import com.eazybytes.jobportal.service.ICompanyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getAllCompanies() {
       List<Company> listOfCompanies=  companyRepository.findAll();
       return listOfCompanies;
    }
}

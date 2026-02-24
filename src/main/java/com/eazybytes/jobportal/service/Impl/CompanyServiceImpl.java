package com.eazybytes.jobportal.service.Impl;

import com.eazybytes.jobportal.dto.CompanyDto;
import com.eazybytes.jobportal.entity.Company;
import com.eazybytes.jobportal.repository.CompanyRepository;
import com.eazybytes.jobportal.service.ICompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;

//    public CompanyServiceImpl(CompanyRepository companyRepository) {
//        this.companyRepository = companyRepository;
//    }

    @Override
    public List<CompanyDto> getAllCompanies() {
       List<Company> listOfCompanies=  companyRepository.findAll();
       return listOfCompanies.stream().map(this::TransformToDto).collect(Collectors.toList());
    }

    private CompanyDto TransformToDto(Company company){
        CompanyDto dto = new CompanyDto(company.getId(),company.getName(), company.getLogo(), company.getIndustry(), company.getSize(), company.getRating(), company.getLocations(), company.getFounded(), company.getDescription(), company.getEmployees(), company.getWebsite(), company.getCreatedAt());
        return dto;
    }
}

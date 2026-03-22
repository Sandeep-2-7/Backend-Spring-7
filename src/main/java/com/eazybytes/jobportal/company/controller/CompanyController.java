package com.eazybytes.jobportal.company.controller;

import com.eazybytes.jobportal.dto.CompanyDto;
import com.eazybytes.jobportal.company.service.ICompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
//@CrossOrigin("http://localhost:5173")
public class CompanyController {

    private final ICompanyService companyService;

//    @Autowired // optional annotation
//    public CompanyController(ICompanyService companyService){
//        this.companyService = companyService;
//    }

    @GetMapping(path="/public", version = "1.0")
    public ResponseEntity<List<CompanyDto>> getAllCompanies(){
        List<CompanyDto> companyList = companyService.getAllCompanies();
//        throw new RuntimeException("AOP Testing");
        return ResponseEntity.ok().body(companyList);

    }
}

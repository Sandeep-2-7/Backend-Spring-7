package com.eazybytes.jobportal.company.controller;

import com.eazybytes.jobportal.dto.CompanyDto;
import com.eazybytes.jobportal.company.service.ICompanyService;
import com.eazybytes.jobportal.entity.Company;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path = "/admin", version = "1.0")
    public ResponseEntity<String> createCompany(@RequestBody @Valid CompanyDto companyDto){
        boolean isCreated = companyService.createCompany(companyDto);
        if(isCreated){
            return ResponseEntity.status(HttpStatus.OK).body("Company created successfully");
        }
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create company");
    }

    @GetMapping(value = "/admin", version = "1.0")
    public ResponseEntity<List<CompanyDto>> getCompaniesForAdmin(){
            List<CompanyDto> companyList = companyService.getAllCompaniesForAdmin();

            return ResponseEntity.status(HttpStatus.OK).body(companyList);
    }

    @PutMapping(value = "{id}/admin", version = "1.0")
    public ResponseEntity<String> updateCompanyDetails(@PathVariable @NotBlank String id, @RequestBody CompanyDto companyDto){
        boolean isUpdated = companyService.updateCompanyDetails(Long.valueOf(id), companyDto);
        if(isUpdated){
            return ResponseEntity.status(HttpStatus.OK).body("Company updated successfully");
        }
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update company details");
    }

    @DeleteMapping(value = "{id}/admin", version = "1.0")
    public ResponseEntity<String> deleteCompanyDetails(@PathVariable @NotBlank String id) {
        companyService.deleteCompanyDetails(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Deleted company details successfully");
    }
}

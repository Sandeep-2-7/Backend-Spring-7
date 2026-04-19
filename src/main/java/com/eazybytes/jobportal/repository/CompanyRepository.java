package com.eazybytes.jobportal.repository;

import com.eazybytes.jobportal.entity.Company;
import jakarta.validation.constraints.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Cacheable(value = "jobs")
    @Query("SELECT DISTINCT c from Company c JOIN FETCH c.jobs j where j.status =:status")
    List<Company> findAllWithJobsByStatus(@Param("status") String status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @CacheEvict(value = "companies", allEntries = true)
    int updateCompanyDetails(@Param("id") Long id,
                             @Param("name") String name,
                             @Param("logo") String logo,
                             @Param("industry") String industry,
                             @Param("size") String size,
                             @Param("rating") BigDecimal rating,
                             @Param("locations") String locations,
                             @Param("founded") Integer founded,
                             @Param("description") String description,
                             @Param("employees") Integer employees,
                             @Param("website") String website);

    @CacheEvict(value = "companies", allEntries = true)
    void deleteById(Long id);

    @CacheEvict(value = "companies", allEntries = true)
    Company save(Company entity);

}
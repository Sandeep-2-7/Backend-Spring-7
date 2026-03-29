package com.eazybytes.jobportal.repository;

import com.eazybytes.jobportal.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT DISTINCT c from Company c JOIN FETCH c.jobs j where j.status =:status")
    List<Company> findAllWithJobsByStatus(@Param("status") String status);
}

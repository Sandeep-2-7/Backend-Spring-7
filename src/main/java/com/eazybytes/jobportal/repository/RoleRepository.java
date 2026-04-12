package com.eazybytes.jobportal.repository;

import com.eazybytes.jobportal.entity.Roles;
import org.apache.catalina.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, Long > {
//    Roles findByName(String name);

    Optional<Roles> findRolesByName(String name);
}

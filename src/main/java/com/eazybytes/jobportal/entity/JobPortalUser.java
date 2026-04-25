package com.eazybytes.jobportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "users")

public class JobPortalUser extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max=255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Email
    @Size(max=255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Size(max = 500)
    @Column(name = "password_hash", nullable = false, length = 500)
    private String passwordHash;

    @Size(max=20)
    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action= OnDeleteAction.SET_NULL)
    @JoinColumn(name = "company_id")
    private Company companyId;

    @OneToOne(mappedBy = "user")
    private Profile profile;

    @ManyToMany
    @JoinTable(name = "saved_jobs",
               joinColumns = {@JoinColumn(name = "user_id")},
               inverseJoinColumns = {@JoinColumn(name = "job_id")})
    private Set<Job> savedJobs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<JobApplication> jobApplications = new LinkedHashSet<>();
}

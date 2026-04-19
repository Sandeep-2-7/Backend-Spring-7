package com.eazybytes.jobportal.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity (name = "profiles")
@Getter
@Setter
@RequiredArgsConstructor
public class Profile extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private JobPortalUser user;

    @NotNull
    @Column(name = "job_title", nullable = false)
    @Size(max=255)
    private String jobTitle;

    @NotNull
    @Column(name = "location", nullable = false)
    @Size(max=255)
    private String location;

    @NotNull
    @Column(name = "experience_level", nullable = false)
    @Size(max=50)
    private String experienceLevel;

    @NotNull
    @Column(name = "professional_bio", nullable = false)
    @Lob
    private String professionalBio;

    @NotNull
    @Column(name = "portfolio_website", nullable = false)
    @Size(max=500)
    private String portfolioWebsite;

    @Column(name = "profile_picture")
    private Byte[] profilePicture;

    @Size(max = 255)
    @Column(name = "profile_picture_name")
    private String profilePictureName;

    @Size(max = 100)
    @Column(name = "profile_picture_type", length = 100)
    private String profilePictureType;

    @Column(name = "resume")
    private byte[] resume;

    @Size(max = 255)
    @Column(name = "resume_name")
    private String resumeName;

    @Size(max = 100)
    @Column(name = "resume_type", length = 100)
    private String resumeType;

}

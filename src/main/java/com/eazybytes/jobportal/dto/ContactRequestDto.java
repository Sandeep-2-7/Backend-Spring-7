package com.eazybytes.jobportal.dto;

import java.io.Serializable;

public record ContactRequestDto(String name, String email, String userType, String subject, String message) implements Serializable {
}

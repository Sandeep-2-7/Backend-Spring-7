package com.eazybytes.jobportal.dto;

import org.springframework.http.HttpStatus;
import java.time.Instant;

public record ErrorResponseDto(String apiPath, String message, HttpStatus errorCode, Instant timestamp) {
}

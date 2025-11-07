package com.mytextile.production.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponseDto(
    String path,
    String message,
    HttpStatus status,
    LocalDateTime timestamp
) {}
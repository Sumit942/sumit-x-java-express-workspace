package com.mytextile.production.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
    String path,
    String message,
    int statusCode,
    LocalDateTime timestamp
) {}
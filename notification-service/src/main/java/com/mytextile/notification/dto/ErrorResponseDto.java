package com.mytextile.notification.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
    String path,
    String message,
    int status,
    LocalDateTime timestamp
) {}
package com.mytextile.inventory.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
    String path,
    String message,
    int httpStatus,
    LocalDateTime timestamp
) {}
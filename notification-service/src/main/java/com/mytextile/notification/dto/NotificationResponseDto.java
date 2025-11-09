package com.mytextile.notification.dto;

import com.mytextile.notification.entity.NotificationStatus;

// This DTO is returned immediately after a request is accepted.
public record NotificationResponseDto(
    Long logId,
    NotificationStatus status,
    String message
) {}
package com.mytextile.notification.dto;

import com.mytextile.notification.entity.ChannelType;
import com.mytextile.notification.entity.NotificationStatus;
import java.time.LocalDateTime;

// This DTO is used when a client wants to read a full notification log.
public record NotificationLogDto(
    Long logId,
    Long clientId,
    ChannelType channel,
    String recipient,
    String subject,
    String body,
    NotificationStatus status,
    LocalDateTime sentAt,
    LocalDateTime createdAt
) {}
package com.mytextile.notification.dto;

import com.mytextile.notification.entity.ChannelType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

// This DTO is used by other services to request a notification.
public record NotificationRequestDto(
    Long clientId, // For logging purposes

    @NotNull(message = "Channel type cannot be null")
    ChannelType channel,

    @NotEmpty(message = "Recipient cannot be empty")
    // @Email // You can add @Email, but this field is also used for phone numbers
    String recipient,

    String subject, // Required for EMAIL, optional for SMS

    @NotEmpty(message = "Body cannot be empty")
    String body
) {}
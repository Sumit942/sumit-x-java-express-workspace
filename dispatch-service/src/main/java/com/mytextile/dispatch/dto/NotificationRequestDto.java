package com.mytextile.dispatch.dto;

// (As provided before)
public record NotificationRequestDto(
        Long clientId, String channel, String recipient, String subject, String body
) {
}
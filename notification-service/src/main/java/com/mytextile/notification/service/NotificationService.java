package com.mytextile.notification.service;

import com.mytextile.notification.dto.NotificationLogDto;
import com.mytextile.notification.dto.NotificationRequestDto;
import com.mytextile.notification.dto.NotificationResponseDto;

import java.util.List;

public interface NotificationService {

    /**
     * Accepts a notification request, saves it, and processes it asynchronously.
     */
    NotificationResponseDto processNotification(NotificationRequestDto requestDto);

    /**
     * Gets a notification log by its ID.
     */
    NotificationLogDto getLogById(Long logId);

    /**
     * Gets all notification logs for a specific client.
     */
    List<NotificationLogDto> getLogsByClientId(Long clientId);
}
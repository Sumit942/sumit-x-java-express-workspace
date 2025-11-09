package com.mytextile.notification.controller;

import com.mytextile.notification.dto.NotificationLogDto;
import com.mytextile.notification.dto.NotificationRequestDto;
import com.mytextile.notification.dto.NotificationResponseDto;
import com.mytextile.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Endpoint to request a new notification.
     * This returns 202 (Accepted) immediately.
     */
    @PostMapping("/send")
    public ResponseEntity<NotificationResponseDto> sendNotification(
            @Valid @RequestBody NotificationRequestDto requestDto) {
        
        NotificationResponseDto response = notificationService.processNotification(requestDto);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    /**
     * Endpoint to check the status/details of a notification.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationLogDto> getNotificationLog(@PathVariable Long id) {
        NotificationLogDto logDto = notificationService.getLogById(id);
        return ResponseEntity.ok(logDto);
    }

    /**
     * Endpoint to get all notification logs for a specific client.
     */
    @GetMapping("/by-client/{clientId}")
    public ResponseEntity<List<NotificationLogDto>> getLogsByClient(@PathVariable Long clientId) {
        List<NotificationLogDto> logs = notificationService.getLogsByClientId(clientId);
        return ResponseEntity.ok(logs);
    }
}
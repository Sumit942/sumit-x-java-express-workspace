package com.mytextile.notification.service;

import com.mytextile.notification.dto.NotificationLogDto;
import com.mytextile.notification.dto.NotificationRequestDto;
import com.mytextile.notification.dto.NotificationResponseDto;
import com.mytextile.notification.exception.ResourceNotFoundException;
import com.mytextile.notification.mapper.NotificationLogMapper;
import com.mytextile.notification.model.ChannelType;
import com.mytextile.notification.model.NotificationLog;
import com.mytextile.notification.model.NotificationStatus;
import com.mytextile.notification.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationLogRepository logRepository;
    private final NotificationLogMapper logMapper;
    private final EmailSenderService emailSenderService; // Our mock sender

    @Override
    @Transactional
    public NotificationResponseDto sendNotification(NotificationRequestDto requestDto) {
        log.info("Received notification request for: {}", requestDto.recipient());

        // 1. Map DTO to Entity and set status
        NotificationLog logEntry = logMapper.toEntity(requestDto);
        logEntry.setStatus(NotificationStatus.PENDING);

        // 2. Save the PENDING log. This is the end of the synchronous transaction.
        NotificationLog savedLog = logRepository.save(logEntry);
        
        // 3. Trigger the asynchronous processing
        processNotificationAsync(savedLog.getLogId());

        // 4. Return an "Accepted" response immediately to the caller
        return new NotificationResponseDto(
            savedLog.getLogId(),
            savedLog.getStatus(),
            "Notification request accepted and is being processed."
        );
    }

    /**
     * This method runs in a separate thread.
     * It needs to be public to be proxied by @Async.
     */
    @Async
    @Transactional
    public void processNotificationAsync(Long logId) {
        log.info("ASYNC: Processing notification for log ID: {}", logId);
        
        NotificationLog logEntry = logRepository.findById(logId)
            .orElse(null); // Should not happen, but good to check

        if (logEntry == null) {
            log.error("ASYNC: Log entry {} not found. Aborting.", logId);
            return;
        }

        try {
            if (logEntry.getChannel() == ChannelType.EMAIL) {
                // 5. Try to send the email
                emailSenderService.sendEmail(
                    logEntry.getRecipient(),
                    logEntry.getSubject(),
                    logEntry.getBody()
                );
                
                // 6. Update log to SENT
                logEntry.setStatus(NotificationStatus.SENT);
                logEntry.setSentAt(LocalDateTime.now());

            } else {
                log.warn("ASYNC: SMS channel not implemented. Marking as FAILED.");
                logEntry.setStatus(NotificationStatus.FAILED);
            }

        } catch (Exception e) {
            log.error("ASYNC: Failed to send notification for log ID: {}", logId, e);
            // 7. Update log to FAILED
            logEntry.setStatus(NotificationStatus.FAILED);
        }
        
        // 8. Save the final status
        logRepository.save(logEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationLogDto getLogById(Long logId) {
        NotificationLog logEntry = logRepository.findById(logId)
            .orElseThrow(() -> new ResourceNotFoundException("NotificationLog", "id", logId));
        return logMapper.toDto(logEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationLogDto> getLogsByClientId(Long clientId) {
        return logRepository.findAllByClientId(clientId)
                .stream()
                .map(logMapper::toDto)
                .collect(Collectors.toList());
    }
}
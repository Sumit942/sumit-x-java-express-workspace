package com.mytextile.notification.service;

import com.mytextile.notification.dto.NotificationLogDto;
import com.mytextile.notification.dto.NotificationRequestDto;
import com.mytextile.notification.dto.NotificationResponseDto;
import com.mytextile.notification.entity.ChannelType;
import com.mytextile.notification.entity.NotificationLog;
import com.mytextile.notification.entity.NotificationStatus;
import com.mytextile.notification.exception.ResourceNotFoundException;
import com.mytextile.notification.mapper.NotificationLogMapper;
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
    public NotificationResponseDto processNotification(NotificationRequestDto requestDto) {
        log.info("Receiving notification request for: {}", requestDto.recipient());

        // 1. Create and save the log entry in PENDING state
        NotificationLog logEntry = new NotificationLog();
        logEntry.setClientId(requestDto.clientId());
        logEntry.setChannel(requestDto.channel());
        logEntry.setRecipient(requestDto.recipient());
        logEntry.setSubject(requestDto.subject());
        logEntry.setBody(requestDto.body());
        logEntry.setStatus(NotificationStatus.PENDING);

        NotificationLog savedLog = logRepository.save(logEntry);

        // 2. Trigger the async sending
        if (requestDto.channel() == ChannelType.EMAIL) {
            sendEmailAsync(savedLog);
        } else {
            // TODO: Implement SMS sending
            log.warn("SMS sending not implemented. Marking as failed.");
            savedLog.setStatus(NotificationStatus.FAILED);
            logRepository.save(savedLog);
        }

        // 3. Return an "Accepted" response immediately
        return new NotificationResponseDto(
            savedLog.getLogId(),
            savedLog.getStatus(),
            "Notification request accepted and is being processed."
        );
    }

    @Async // This tells Spring to run this in a separate thread
    @Transactional
    public void sendEmailAsync(NotificationLog logEntry) {
        log.info("ASYNC: Processing email for log ID: {}", logEntry.getLogId());
        try {
            // 4. Try to send the email
            emailSenderService.sendEmail(
                logEntry.getRecipient(),
                logEntry.getSubject(),
                logEntry.getBody()
            );

            // 5. Update log to SENT
            logEntry.setStatus(NotificationStatus.SENT);
            logEntry.setSentAt(LocalDateTime.now());

        } catch (Exception e) {
            log.error("ASYNC: Failed to send email for log ID: {}", logEntry.getLogId(), e);
            // 6. Update log to FAILED
            logEntry.setStatus(NotificationStatus.FAILED);
        }
        
        // 7. Save the final status
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
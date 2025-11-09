package com.mytextile.notification.mapper;

import com.mytextile.notification.dto.NotificationLogDto;
import com.mytextile.notification.entity.NotificationLog;
import org.springframework.stereotype.Component;

@Component
public class NotificationLogMapper {

    /**
     * Converts a NotificationLog Entity to its DTO representation.
     */
    public NotificationLogDto toDto(NotificationLog log) {
        if (log == null) {
            return null;
        }

        return new NotificationLogDto(
            log.getLogId(),
            log.getClientId(),
            log.getChannel(),
            log.getRecipient(),
            log.getSubject(),
            log.getBody(),
            log.getStatus(),
            log.getSentAt(),
            log.getCreatedAt()
        );
    }

    // Note: We don't have a toEntity() method because the service
    // will construct the NotificationLog from the NotificationRequestDto,
    // which is not a 1:1 mapping.
}
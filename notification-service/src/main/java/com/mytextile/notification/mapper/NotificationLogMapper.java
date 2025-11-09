package com.mytextile.notification.mapper;

import com.mytextile.notification.dto.NotificationLogDto;
import com.mytextile.notification.dto.NotificationRequestDto;
import com.mytextile.notification.model.NotificationLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationLogMapper {

    /**
     * Converts a request DTO into a Log entity.
     */
    @Mapping(target = "logId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "sentAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    NotificationLog toEntity(NotificationRequestDto dto);
    
    /**
     * Converts a Log entity into its full DTO.
     */
    NotificationLogDto toDto(NotificationLog log);
}
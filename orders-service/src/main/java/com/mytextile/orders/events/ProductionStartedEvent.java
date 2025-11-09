package com.mytextile.orders.events;
import java.time.LocalDateTime;
// DTO for event CONSUMED from Production Service
public record ProductionStartedEvent(
    Long jobId,
    Long orderId,
    Long machineId,
    LocalDateTime startedAt
) {}
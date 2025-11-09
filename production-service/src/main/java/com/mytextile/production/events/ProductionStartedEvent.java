package com.mytextile.production.events;

import java.time.LocalDateTime;

public record ProductionStartedEvent(
    Long jobId,
    Long orderId,
    Long machineId,
    LocalDateTime startedAt
) {}
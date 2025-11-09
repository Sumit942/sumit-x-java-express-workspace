package com.mytextile.dispatch.events.dto;

import java.math.BigDecimal;
// DTO for event CONSUMED from Production Service
// Note: We only care about the output
public record ProductionCompletedEvent(
    Long orderId,
    String outputItemSku,
    BigDecimal actualOutputQuantity
) {}
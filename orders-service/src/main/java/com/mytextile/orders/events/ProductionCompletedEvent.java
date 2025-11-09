package com.mytextile.orders.events;
import java.math.BigDecimal;
import java.util.List;
// DTO for event CONSUMED from Production Service
public record ProductionCompletedEvent(
    Long jobId,
    Long orderId,
    String outputItemSku,
    BigDecimal actualOutputQuantity,
    List<ProductionInputDto> inputsConsumed
) {}
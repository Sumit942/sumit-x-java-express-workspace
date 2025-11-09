package com.mytextile.production.events;

import java.math.BigDecimal;
import java.util.List;

public record ProductionCompletedEvent(
    Long jobId,
    Long orderId,
    String outputItemSku,
    BigDecimal actualOutputQuantity,
    List<ProductionInputDto> inputsConsumed
) {}
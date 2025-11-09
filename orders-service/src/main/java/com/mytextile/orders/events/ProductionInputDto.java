package com.mytextile.orders.events;
import java.math.BigDecimal;
// DTO for event CONSUMED from Production Service
public record ProductionInputDto(
    String inputItemSku,
    BigDecimal quantityConsumed
) {}
package com.mytextile.inventory.events.dto;
import java.math.BigDecimal;
// DTO for event CONSUMED from Dispatch Service
public record DispatchedItemDto(
    String itemSku,
    BigDecimal quantity
) {}
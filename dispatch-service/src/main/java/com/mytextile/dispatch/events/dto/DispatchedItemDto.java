package com.mytextile.dispatch.events.dto;
import java.math.BigDecimal;
// DTO for event PRODUCED by this service
public record DispatchedItemDto(
    String itemSku,
    BigDecimal quantity
) {}
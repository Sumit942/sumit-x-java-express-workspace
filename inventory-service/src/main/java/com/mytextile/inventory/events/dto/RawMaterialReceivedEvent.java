package com.mytextile.inventory.events.dto;
import java.math.BigDecimal;
// DTO for event PRODUCED by this service
public record RawMaterialReceivedEvent(
    Long orderId,
    Long clientId,
    String sku,
    BigDecimal quantity,
    Long ledgerId
) {}
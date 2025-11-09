package com.mytextile.inventory.events.dto;
import java.util.List;
// DTO for event CONSUMED from Dispatch Service
public record ProductDispatchedEvent(
    Long shipmentId,
    Long orderId,
    Long clientId,
    List<DispatchedItemDto> itemsDispatched
) {}
package com.mytextile.dispatch.events.dto;
import java.util.List;
// DTO for event PRODUCED by this service
public record ProductDispatchedEvent(
    Long shipmentId,
    Long orderId,
    Long clientId,
    List<DispatchedItemDto> itemsDispatched
) {}
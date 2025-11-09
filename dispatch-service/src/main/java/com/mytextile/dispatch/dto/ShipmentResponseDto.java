package com.mytextile.dispatch.dto;

import com.mytextile.dispatch.entity.ShipmentStatus;

import java.time.LocalDateTime;
import java.util.List;

// "Read" DTO for a full shipment, sent as a response
public record ShipmentResponseDto(
    Long shipmentId,
    Long orderId,
    Long clientId,
    String challanNumber,
    String shippingAddress,
    LocalDateTime dispatchDate,
    ShipmentStatus status,
    List<ShipmentItemDto> items
) {}
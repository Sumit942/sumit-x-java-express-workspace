package com.mytextile.dispatch.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

// DTO for the POST request to create a new shipment
public record CreateShipmentRequestDto(
    @NotNull(message = "Order ID cannot be null")
    Long orderId,
    
    @NotNull(message = "Client ID cannot be null")
    Long clientId,
    
    @NotEmpty(message = "Shipping address cannot be empty")
    String shippingAddress,
    
    @Valid // This ensures the nested items are also validated
    @NotEmpty(message = "Shipment must contain at least one item")
    List<ShipmentItemRequestDto> items
) {}
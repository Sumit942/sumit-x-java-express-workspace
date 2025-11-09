package com.mytextile.orders.dto;

import jakarta.validation.constraints.NotNull;

public record CreateOrderDto(
    @NotNull(message = "Client ID cannot be null")
    Long clientId,
    
    String orderDescription
) {}
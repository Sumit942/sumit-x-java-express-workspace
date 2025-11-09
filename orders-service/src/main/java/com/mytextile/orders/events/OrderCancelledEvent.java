package com.mytextile.orders.events;
// DTO for event PRODUCED by this service
public record OrderCancelledEvent(
    Long orderId
) {}
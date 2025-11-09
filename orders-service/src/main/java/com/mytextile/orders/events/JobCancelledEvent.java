package com.mytextile.orders.events;
// DTO for event CONSUMED from Production Service
public record JobCancelledEvent(
    Long jobId,
    Long orderId
) {}
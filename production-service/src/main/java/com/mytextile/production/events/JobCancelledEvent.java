package com.mytextile.production.events;

public record JobCancelledEvent(
    Long jobId,
    Long orderId
) {}
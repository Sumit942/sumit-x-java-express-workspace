package com.mytextile.orders.model;

public enum OrderStatus {
    AWAITING_MATERIAL,      // Initial state
    AWAITING_PRODUCTION,    // Material received
    IN_PRODUCTION,          // Job started on machine
    PRODUCTION_COMPLETE,    // Job finished
    PARTIALLY_DISPATCHED,   // Part of the order shipped
    COMPLETED,              // Full order shipped
    CANCELLED               // Order cancelled
}
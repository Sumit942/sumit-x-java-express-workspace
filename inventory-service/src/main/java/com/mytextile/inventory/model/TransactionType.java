package com.mytextile.inventory.model;

public enum TransactionType {
    INWARD_RAW,         // Raw material received from client
    PRODUCTION_CONSUMED,  // Raw material consumed by production
    PRODUCTION_COMPLETED, // Finished good created by production
    OUTWARD_DISPATCH,     // Finished good shipped to client
    ADJUSTMENT          // Manual stock correction
}
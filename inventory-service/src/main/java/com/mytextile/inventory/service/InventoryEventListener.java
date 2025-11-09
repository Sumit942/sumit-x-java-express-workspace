package com.mytextile.inventory.service;

import com.mytextile.inventory.events.dto.DispatchedItemDto;
import com.mytextile.inventory.events.dto.ProductDispatchedEvent;
import com.mytextile.inventory.events.dto.ProductionCompletedEvent;
import com.mytextile.inventory.model.InventoryItem;
import com.mytextile.inventory.model.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InventoryEventListener {

    // Using InventoryServiceImpl as it contains the protected helper methods
    private final InventoryServiceImpl inventoryService; 

    @KafkaListener(topics = "production_completed_topic", groupId = "inventory-service-group")
    public void handleProductionCompleted(ProductionCompletedEvent event) {
        log.info("Handling production_completed event for order: {}", event.orderId());
        
        try {
            // 1. Add the finished good to stock
            InventoryItem finishedGood = inventoryService.findItemBySku(event.outputItemSku());
            inventoryService.addLedgerEntry(
                    finishedGood,
                    event.orderId(),
                    null, // Client ID isn't needed here
                    event.actualOutputQuantity(), // Positive quantity
                    TransactionType.PRODUCTION_COMPLETED
            );
            
            // 2. Consume (remove) the raw materials from stock
            event.inputsConsumed().forEach(input -> {
                InventoryItem rawMaterial = inventoryService.findItemBySku(input.inputItemSku());
                inventoryService.addLedgerEntry(
                        rawMaterial,
                        event.orderId(),
                        null,
                        input.quantityConsumed().negate(), // Negative quantity
                        TransactionType.PRODUCTION_CONSUMED
                );
            });
            
        } catch (Exception e) {
            log.error("Failed to process production_completed event: {}. Error: {}", event, e.getMessage());
            // This should trigger a retry/dead-letter-queue logic
            throw new RuntimeException("Failed to process event, will retry.", e);
        }
    }

    @KafkaListener(topics = "product_dispatched_topic", groupId = "inventory-service-group")
    public void handleProductDispatched(ProductDispatchedEvent event) {
        log.info("Handling product_dispatched event for order: {}", event.orderId());

        try {
            // 1. Remove each dispatched item from stock
            for (DispatchedItemDto itemDto : event.itemsDispatched()) {
                InventoryItem item = inventoryService.findItemBySku(itemDto.itemSku());
                inventoryService.addLedgerEntry(
                        item,
                        event.orderId(),
                        event.clientId(),
                        itemDto.quantity().negate(), // Negative quantity
                        TransactionType.OUTWARD_DISPATCH
                );
            }
        } catch (Exception e) {
            log.error("Failed to process product_dispatched event: {}. Error: {}", event, e.getMessage());
            throw new RuntimeException("Failed to process event, will retry.", e);
        }
    }
}
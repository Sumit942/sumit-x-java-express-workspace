package com.mytextile.dispatch.service;

import com.mytextile.dispatch.events.dto.ProductionCompletedEvent;
import com.mytextile.dispatch.model.DispatchableStock;
import com.mytextile.dispatch.repository.DispatchableStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DispatchEventListener {

    private final DispatchableStockRepository stockRepository;

    @KafkaListener(topics = "production_completed_topic", groupId = "dispatch-service-group")
    public void handleProductionCompleted(ProductionCompletedEvent event) {
        log.info("Handling production_completed event for order: {}", event.orderId());
        
        try {
            DispatchableStock stock = stockRepository.findByOrderIdAndItemSku(
                    event.orderId(), event.outputItemSku())
                    .orElse(new DispatchableStock());
            
            if (stock.getId() == null) { // New stock item
                stock.setOrderId(event.orderId());
                stock.setItemSku(event.outputItemSku());
                stock.setAvailableQuantity(event.actualOutputQuantity());
            } else { // Add to existing stock
                stock.setAvailableQuantity(
                    stock.getAvailableQuantity().add(event.actualOutputQuantity())
                );
            }
            
            stockRepository.save(stock);
            log.info("Updated dispatchable stock for order {}: SKU {} now has {} available.",
                event.orderId(), event.outputItemSku(), stock.getAvailableQuantity());

        } catch (Exception e) {
            log.error("Failed to process production_completed event: {}. Error: {}", event, e.getMessage());
            // This should trigger a retry/dead-letter-queue logic
            throw new RuntimeException("Failed to process event, will retry.", e);
        }
    }
}
package com.mytextile.orders.service;

import com.mytextile.orders.events.JobCancelledEvent;
import com.mytextile.orders.events.ProductionCompletedEvent;
import com.mytextile.orders.events.ProductionStartedEvent;
import com.mytextile.orders.model.OrderStatus;
import com.mytextile.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final OrderRepository orderRepository;

    // TODO: This should listen for RAW_MATERIAL_RECEIVED from Inventory Service
    // For now, we'll start with Production listeners.

    @KafkaListener(topics = "production_started_topic", groupId = "order-service-group")
    @Transactional
    public void handleProductionStarted(ProductionStartedEvent event) {
        log.info("Received production_started event for order: {}", event.orderId());
        
        orderRepository.findById(event.orderId()).ifPresent(order -> {
            order.setStatus(OrderStatus.IN_PRODUCTION);
            orderRepository.save(order);
        });
    }

    @KafkaListener(topics = "production_completed_topic", groupId = "order-service-group")
    @Transactional
    public void handleProductionCompleted(ProductionCompletedEvent event) {
        log.info("Received production_completed event for order: {}", event.orderId());
        
        orderRepository.findById(event.orderId()).ifPresent(order -> {
            order.setStatus(OrderStatus.PRODUCTION_COMPLETE);
            orderRepository.save(order);
        });
    }

    @KafkaListener(topics = "job_cancelled_topic", groupId = "order-service-group")
    @Transactional
    public void handleJobCancelled(JobCancelledEvent event) {
        log.info("Received job_cancelled event for order: {}", event.orderId());
        
        orderRepository.findById(event.orderId()).ifPresent(order -> {
            // Only update status if it's not *already* user-cancelled
            if (order.getStatus() != OrderStatus.CANCELLED) {
                // This state means the job was cancelled, but the order might be re-queued
                // We'll set it back to AWAITING_PRODUCTION for now
                order.setStatus(OrderStatus.AWAITING_PRODUCTION); 
                orderRepository.save(order);
            }
        });
    }
    
    // TODO: Add listener for 'PRODUCT_DISPATCHED' from Dispatch Service
    // This listener would set status to PARTIALLY_DISPATCHED or COMPLETED.
}
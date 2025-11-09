package com.mytextile.dispatch.service.impl;

import com.mytextile.dispatch.dto.CreateShipmentRequestDto;
import com.mytextile.dispatch.dto.ShipmentResponseDto;
import com.mytextile.dispatch.entity.Shipment;
import com.mytextile.dispatch.entity.ShipmentStatus;
import com.mytextile.dispatch.exception.ResourceNotFoundException;
import com.mytextile.dispatch.mapper.ShipmentMapper;
import com.mytextile.dispatch.repository.ShipmentRepository;
import com.mytextile.dispatch.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    // Inject your Kafka/RabbitMQ producer here to send events
    // private final KafkaTemplate<String, ProductDispatchedEvent> kafkaTemplate;

    @Override
    public ShipmentResponseDto createShipment(CreateShipmentRequestDto requestDto) {
        log.info("Creating new shipment for order ID: {}", requestDto.orderId());

        // 1. Map DTO to Entity
        Shipment shipment = shipmentMapper.toEntity(requestDto);

        // 2. Apply Business Logic
        shipment.setChallanNumber(generateChallanNumber(requestDto.orderId()));
        shipment.setStatus(ShipmentStatus.PENDING); // Initial status
        shipment.setDispatchDate(LocalDateTime.now()); // Or set when it actually ships

        // 3. Save Entity
        Shipment savedShipment = shipmentRepository.save(shipment);

        // 4. Publish Event (Crucial for microservices)
        // This event tells InventoryService to update stock
        // and OrderService to update status.
        log.info("Publishing PRODUCT_DISPATCHED event for order {}", savedShipment.getOrderId());
        // ProductDispatchedEvent event = new ProductDispatchedEvent(...);
        // kafkaTemplate.send("product_dispatched_topic", event);

        // 5. Map back to DTO for response
        return shipmentMapper.toDto(savedShipment);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponseDto getShipmentById(Long shipmentId) {
        log.info("Fetching shipment with ID: {}", shipmentId);
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment", "id", shipmentId));
        
        return shipmentMapper.toDto(shipment);
    }

    // --- Helper Methods ---

    private String generateChallanNumber(Long orderId) {
        // Example: CH-12345-ABCD
        String shortUuid = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return String.format("CH-%s-%s", orderId, shortUuid);
    }
}
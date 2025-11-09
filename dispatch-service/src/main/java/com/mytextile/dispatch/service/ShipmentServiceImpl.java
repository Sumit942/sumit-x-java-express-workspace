package com.mytextile.dispatch.service;

import com.mytextile.dispatch.dto.CreateShipmentRequestDto;
import com.mytextile.dispatch.dto.ShipmentItemRequestDto;
import com.mytextile.dispatch.dto.ShipmentResponseDto;
import com.mytextile.dispatch.events.dto.DispatchedItemDto;
import com.mytextile.dispatch.events.dto.ProductDispatchedEvent;
import com.mytextile.dispatch.exception.BusinessLogicException;
import com.mytextile.dispatch.exception.ResourceNotFoundException;
import com.mytextile.dispatch.mapper.ShipmentMapper;
import com.mytextile.dispatch.model.DispatchableStock;
import com.mytextile.dispatch.model.Shipment;
import com.mytextile.dispatch.model.ShipmentItem;
import com.mytextile.dispatch.model.ShipmentStatus;
import com.mytextile.dispatch.repository.DispatchableStockRepository;
import com.mytextile.dispatch.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final DispatchableStockRepository stockRepository;
    private final ShipmentMapper shipmentMapper;
    private final KafkaTemplate<String, ProductDispatchedEvent> kafkaTemplate;

    @Override
    public ShipmentResponseDto createShipment(CreateShipmentRequestDto requestDto) {
        log.info("Creating new shipment for order: {}", requestDto.orderId());
        
        // 1. Validate stock and build ShipmentItems
        Shipment shipment = new Shipment();
        for (ShipmentItemRequestDto itemDto : requestDto.items()) {
            DispatchableStock stock = checkAndLockStock(requestDto.orderId(), itemDto.itemSku(), itemDto.quantity());
            
            ShipmentItem shipmentItem = new ShipmentItem();
            shipmentItem.setItemSku(itemDto.itemSku());
            shipmentItem.setQuantity(itemDto.quantity());
            shipment.addItem(shipmentItem);

            // 2. Update the stock
            stock.setAvailableQuantity(stock.getAvailableQuantity().subtract(itemDto.quantity()));
            stockRepository.save(stock);
        }

        // 3. Populate rest of shipment and save
        shipment.setOrderId(requestDto.orderId());
        shipment.setClientId(requestDto.clientId());
        shipment.setShippingAddress(requestDto.shippingAddress());
        shipment.setChallanNumber(generateChallanNumber(requestDto.orderId()));
        shipment.setStatus(ShipmentStatus.SHIPPED); // Assume it's shipped immediately
        shipment.setDispatchDate(LocalDateTime.now());
        
        Shipment savedShipment = shipmentRepository.save(shipment);

        // 4. Publish ProductDispatchedEvent
        List<DispatchedItemDto> dispatchedItems = savedShipment.getItems().stream()
                .map(item -> new DispatchedItemDto(item.getItemSku(), item.getQuantity()))
                .toList();
        
        ProductDispatchedEvent event = new ProductDispatchedEvent(
            savedShipment.getShipmentId(),
            savedShipment.getOrderId(),
            savedShipment.getClientId(),
            dispatchedItems
        );
        
        kafkaTemplate.send("product_dispatched_topic", event);
        log.info("Published ProductDispatchedEvent for order {}", savedShipment.getOrderId());

        return shipmentMapper.toDto(savedShipment);
    }

    private DispatchableStock checkAndLockStock(Long orderId, String sku, BigDecimal requestedQty) {
        DispatchableStock stock = stockRepository.findByOrderIdAndItemSku(orderId, sku)
            .orElseThrow(() -> new ResourceNotFoundException("DispatchableStock", "SKU", sku));
        
        if (stock.getAvailableQuantity().compareTo(requestedQty) < 0) {
            throw new BusinessLogicException(String.format(
                "Not enough stock for SKU %s. Requested: %s, Available: %s",
                sku, requestedQty, stock.getAvailableQuantity()
            ));
        }
        return stock;
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponseDto getShipmentById(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment", "id", shipmentId));
        return shipmentMapper.toDto(shipment);
    }
    
    private String generateChallanNumber(Long orderId) {
        String shortUuid = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return String.format("CH-%s-%s", orderId, shortUuid);
    }
}
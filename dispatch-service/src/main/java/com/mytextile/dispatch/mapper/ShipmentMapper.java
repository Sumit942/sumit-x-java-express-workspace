// --- File: src/main/java/com/mytextile/dispatch/mapper/ShipmentMapper.java ---
package com.mytextile.dispatch.mapper;

import com.mytextile.dispatch.dto.CreateShipmentRequestDto;
import com.mytextile.dispatch.dto.ShipmentItemDto;
import com.mytextile.dispatch.dto.ShipmentItemRequestDto;
import com.mytextile.dispatch.dto.ShipmentResponseDto;
import com.mytextile.dispatch.entity.Shipment;
import com.mytextile.dispatch.entity.ShipmentItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ShipmentMapper {

    /**
     * Converts a Shipment Entity to a ShipmentResponseDto (for sending to client).
     */
    public ShipmentResponseDto toDto(Shipment shipment) {
        if (shipment == null) {
            return null;
        }

        return new ShipmentResponseDto(
            shipment.getShipmentId(),
            shipment.getOrderId(),
            shipment.getClientId(),
            shipment.getChallanNumber(),
            shipment.getShippingAddress(),
            shipment.getDispatchDate(),
            shipment.getStatus(),
            shipment.getItems().stream()
                    .map(this::itemToDto) // Use helper to convert each item
                    .collect(Collectors.toList())
        );
    }

    /**
     * Converts a CreateShipmentRequestDto (from client) to a Shipment Entity.
     * Note: This does NOT set challanNumber, status, or dispatchDate.
     * The Service layer should be responsible for that business logic.
     */
    public Shipment toEntity(CreateShipmentRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Shipment shipment = new Shipment();
        shipment.setOrderId(dto.orderId());
        shipment.setClientId(dto.clientId());
        shipment.setShippingAddress(dto.shippingAddress());

        // Map and add items, setting the bidirectional relationship
        if (dto.items() != null) {
            dto.items().stream()
                .map(this::itemRequestToEntity) // Use helper to convert item DTO
                .forEach(shipment::addItem); // Use helper on Shipment entity
        }
        
        return shipment;
    }

    // --- Helper Methods ---

    /**
     * Converts a ShipmentItem Entity to its DTO.
     */
    private ShipmentItemDto itemToDto(ShipmentItem item) {
        if (item == null) {
            return null;
        }
        return new ShipmentItemDto(
            item.getItemId(),
            item.getItemSku(),
            item.getQuantity()
        );
    }

    /**
     * Converts a ShipmentItemRequestDto to its Entity.
     * Note: The 'shipment' field is NOT set here. It's set by the
     * 'shipment.addItem()' method, which handles the relationship.
     */
    private ShipmentItem itemRequestToEntity(ShipmentItemRequestDto itemDto) {
        if (itemDto == null) {
            return null;
        }
        ShipmentItem item = new ShipmentItem();
        item.setItemSku(itemDto.itemSku());
        item.setQuantity(itemDto.quantity());
        return item;
    }
}
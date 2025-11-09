package com.mytextile.dispatch.mapper;

import com.mytextile.dispatch.dto.ShipmentItemDto;
import com.mytextile.dispatch.dto.ShipmentResponseDto;
import com.mytextile.dispatch.model.Shipment;
import com.mytextile.dispatch.model.ShipmentItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {
    
    ShipmentItemDto toItemDto(ShipmentItem item);
    ShipmentResponseDto toDto(Shipment shipment);
}
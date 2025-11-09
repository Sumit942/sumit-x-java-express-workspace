package com.mytextile.dispatch.mapper;

import com.mytextile.dispatch.dto.InvoiceResponseDto;
import com.mytextile.dispatch.model.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    @Mapping(target = "shipmentId", source = "shipment.shipmentId")
    InvoiceResponseDto toDto(Invoice invoice);
}
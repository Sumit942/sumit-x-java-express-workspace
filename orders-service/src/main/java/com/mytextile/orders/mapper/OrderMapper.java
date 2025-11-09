package com.mytextile.orders.mapper;

import com.mytextile.orders.dto.CreateOrderDto;
import com.mytextile.orders.dto.OrderDto;
import com.mytextile.orders.model.ClientOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toDto(ClientOrder clientOrder);

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ClientOrder toEntity(CreateOrderDto createOrderDto);
}
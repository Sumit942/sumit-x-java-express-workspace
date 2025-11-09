package com.mytextile.orders.service;

import com.mytextile.orders.dto.CreateOrderDto;
import com.mytextile.orders.dto.OrderDto;
import java.util.List;

public interface OrderService {

    OrderDto createOrder(CreateOrderDto dto);

    OrderDto getOrderById(Long orderId);

    List<OrderDto> getOrdersByClientId(Long clientId);

    OrderDto cancelOrder(Long orderId);
}
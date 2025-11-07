package com.mytextile.orders.service;

import com.mytextile.orders.dto.ClientOrderDto;

import java.util.List;

public interface IClientOrderService {

    ClientOrderDto createOrder(ClientOrderDto clientOrderDto);
    ClientOrderDto getOrderById(Long orderId);
    List<ClientOrderDto> getAllOrders();
}

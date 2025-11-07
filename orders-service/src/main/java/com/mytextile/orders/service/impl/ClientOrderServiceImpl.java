package com.mytextile.orders.service.impl;

import com.mytextile.orders.dto.ClientOrderDto;
import com.mytextile.orders.entity.ClientOrder;
import com.mytextile.orders.exception.ResourceNotFoundException;
import com.mytextile.orders.mapper.ClientOrderMapper;
import com.mytextile.orders.repository.ClientOrderRepository;
import com.mytextile.orders.service.IClientOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClientOrderServiceImpl implements IClientOrderService {

    private final ClientOrderRepository orderRepository;
    private final ClientOrderMapper orderMapper;

    @Override
    public ClientOrderDto createOrder(ClientOrderDto clientOrderDto) {
        ClientOrder clientOrder = orderMapper.toEntity(clientOrderDto);
        ClientOrder savedOrder = orderRepository.save(clientOrder);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public ClientOrderDto getOrderById(Long orderId) {
        ClientOrder clientOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("clientOrder", "id", orderId));
        return orderMapper.toDto(clientOrder);
    }

    @Override
    public List<ClientOrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
}

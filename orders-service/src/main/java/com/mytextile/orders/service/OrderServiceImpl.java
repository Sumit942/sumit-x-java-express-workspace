package com.mytextile.orders.service;

import com.mytextile.orders.dto.CreateOrderDto;
import com.mytextile.orders.dto.OrderDto;
import com.mytextile.orders.events.OrderCancelledEvent;
import com.mytextile.orders.exception.BusinessLogicException;
import com.mytextile.orders.exception.ResourceNotFoundException;
import com.mytextile.orders.mapper.OrderMapper;
import com.mytextile.orders.model.ClientOrder;
import com.mytextile.orders.model.OrderStatus;
import com.mytextile.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final KafkaTemplate<String, OrderCancelledEvent> kafkaTemplate;

    @Override
    public OrderDto createOrder(CreateOrderDto dto) {
        log.info("Creating new order for client: {}", dto.clientId());
        ClientOrder order = orderMapper.toEntity(dto);
        
        // Set the initial status
        order.setStatus(OrderStatus.AWAITING_MATERIAL); 
        
        ClientOrder savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long orderId) {
        ClientOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByClientId(Long clientId) {
        return orderRepository.findByClientId(clientId).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto cancelOrder(Long orderId) {
        log.info("Attempting to cancel order: {}", orderId);
        ClientOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        // Business Rule: Can't cancel an order that's already shipped
        if (order.getStatus() == OrderStatus.COMPLETED || 
            order.getStatus() == OrderStatus.PARTIALLY_DISPATCHED) {
            throw new BusinessLogicException("Cannot cancel an order that has already been dispatched.");
        }
        
        if (order.getStatus() == OrderStatus.CANCELLED) {
             throw new BusinessLogicException("Order is already cancelled.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        ClientOrder savedOrder = orderRepository.save(order);
        
        // Publish event for other services (like Production) to react
        OrderCancelledEvent event = new OrderCancelledEvent(orderId);
        kafkaTemplate.send("order_cancelled_topic", event);
        log.info("Published OrderCancelledEvent for order: {}", orderId);

        return orderMapper.toDto(savedOrder);
    }
}
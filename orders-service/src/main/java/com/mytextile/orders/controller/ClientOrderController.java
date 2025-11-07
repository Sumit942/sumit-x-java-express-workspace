package com.mytextile.orders.controller;

import com.mytextile.orders.dto.ClientOrderDto;
import com.mytextile.orders.service.IClientOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class ClientOrderController {
    private final IClientOrderService orderService;

    @PostMapping
    public ResponseEntity<ClientOrderDto> createClient(@Valid @RequestBody ClientOrderDto orderDto) {
        ClientOrderDto createdClient = orderService.createOrder(orderDto);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientOrderDto> getClientById(@PathVariable("id") Long clientId) {
        ClientOrderDto client = orderService.getOrderById(clientId);
        return ResponseEntity.ok(client);
    }

    @GetMapping
    public ResponseEntity<List<ClientOrderDto>> getAllClients() {
        List<ClientOrderDto> clients = orderService.getAllOrders();
        return ResponseEntity.ok(clients);
    }
}

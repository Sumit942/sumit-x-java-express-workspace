package com.mytextile.dispatch.controller;

import com.mytextile.dispatch.dto.CreateShipmentRequestDto;
import com.mytextile.dispatch.dto.ShipmentResponseDto;
import com.mytextile.dispatch.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping
    public ResponseEntity<ShipmentResponseDto> createShipment(
            @Valid @RequestBody CreateShipmentRequestDto requestDto) {
        
        ShipmentResponseDto createdShipment = shipmentService.createShipment(requestDto);
        return new ResponseEntity<>(createdShipment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponseDto> getShipmentById(@PathVariable("id") Long shipmentId) {
        return ResponseEntity.ok(shipmentService.getShipmentById(shipmentId));
    }
}
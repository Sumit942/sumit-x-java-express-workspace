package com.mytextile.inventory.controller;

import com.mytextile.inventory.dto.InventoryItemDto;
import com.mytextile.inventory.service.I_InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory") // Base path for all inventory-related APIs
@RequiredArgsConstructor
public class InventoryController {

    private final I_InventoryService iInventoryService;

    @PostMapping
    public ResponseEntity<InventoryItemDto> createInventoryItem(@Valid @RequestBody InventoryItemDto inventoryItemDto) {
        InventoryItemDto createdInventoryItem = iInventoryService.createItem(inventoryItemDto);
        return new ResponseEntity<>(createdInventoryItem, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemDto> getClientById(@PathVariable("id") Long itemId) {
        InventoryItemDto client = iInventoryService.getItemById(itemId);
        return ResponseEntity.ok(client);
    }

    @GetMapping
    public ResponseEntity<List<InventoryItemDto>> getAllClients() {
        List<InventoryItemDto> clients = iInventoryService.getAllItems();
        return ResponseEntity.ok(clients);
    }

}

package com.mytextile.inventory.service.impl;

import com.mytextile.inventory.dto.InventoryItemDto;
import com.mytextile.inventory.entity.InventoryItem;
import com.mytextile.inventory.exception.ResourceNotFoundException;
import com.mytextile.inventory.mapper.InventoryItemMapper;
import com.mytextile.inventory.repo.InventoryItemRepository;
import com.mytextile.inventory.service.I_InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements I_InventoryService {

    private final InventoryItemRepository itemRepository;
    private final InventoryItemMapper inventoryItemMapper;

    @Override
    public InventoryItemDto createItem(InventoryItemDto inventoryItemDto) {
        InventoryItem inventoryItem = inventoryItemMapper.toEntity(inventoryItemDto);

        InventoryItem savedInventoryItem = itemRepository.save(inventoryItem);

        return inventoryItemMapper.toDto(savedInventoryItem);
    }

    @Override
    public InventoryItemDto getItemById(Long itemId) {
        InventoryItem inventoryItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("inventoryItem", "id", itemId));

        return inventoryItemMapper.toDto(inventoryItem);
    }

    @Override
    public List<InventoryItemDto> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(inventoryItemMapper::toDto)
                .collect(Collectors.toList());
    }
}

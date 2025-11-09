package com.mytextile.inventory.service;

import com.mytextile.inventory.dto.*;
import com.mytextile.inventory.events.dto.RawMaterialReceivedEvent;
import com.mytextile.inventory.exception.BusinessLogicException;
import com.mytextile.inventory.exception.ResourceNotFoundException;
import com.mytextile.inventory.mapper.InventoryMapper;
import com.mytextile.inventory.model.InventoryItem;
import com.mytextile.inventory.model.InventoryLedger;
import com.mytextile.inventory.model.TransactionType;
import com.mytextile.inventory.repository.InventoryItemRepository;
import com.mytextile.inventory.repository.InventoryLedgerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository itemRepository;
    private final InventoryLedgerRepository ledgerRepository;
    private final InventoryMapper inventoryMapper;
    private final KafkaTemplate<String, RawMaterialReceivedEvent> kafkaTemplate;

    @Override
    public ItemDto createItem(CreateItemDto dto) {
        log.info("Creating new inventory item with SKU: {}", dto.sku());
        if (itemRepository.findBySku(dto.sku()).isPresent()) {
            throw new BusinessLogicException("SKU '" + dto.sku() + "' already exists.");
        }
        InventoryItem item = inventoryMapper.toEntity(dto);
        InventoryItem savedItem = itemRepository.save(item);
        return inventoryMapper.toDto(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemBySku(String sku) {
        InventoryItem item = findItemBySku(sku);
        return inventoryMapper.toDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public StockLevelDto getStockLevel(String sku) {
        log.info("Getting stock level for SKU: {}", sku);
        InventoryItem item = findItemBySku(sku);
        BigDecimal stock = ledgerRepository.getStockByItemId(item.getItemId());
        return new StockLevelDto(item.getItemId(), item.getSku(), item.getName(), stock, item.getUnit());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LedgerEntryDto> getLedgerHistory(String sku) {
        log.info("Getting ledger history for SKU: {}", sku);
        InventoryItem item = findItemBySku(sku);
        List<InventoryLedger> ledgers = ledgerRepository.findAllByItemId(item.getItemId());
        
        // Manual mapping because DTO combines two sources
        return ledgers.stream()
                .map(ledger -> new LedgerEntryDto(
                        ledger.getLedgerId(),
                        ledger.getOrderId(),
                        item.getSku(),
                        item.getName(),
                        ledger.getQuantity(),
                        ledger.getTransactionType(),
                        ledger.getTransactionDate()
                ))
                .toList();
    }

    @Override
    public void receiveRawMaterial(InwardRequestDto dto) {
        log.info("Receiving raw material for order: {}, SKU: {}", dto.orderId(), dto.sku());
        InventoryItem item = findItemBySku(dto.sku());
        
        InventoryLedger savedLedger = addLedgerEntry(
                item, 
                dto.orderId(), 
                dto.clientId(), 
                dto.quantity(), 
                TransactionType.INWARD_RAW
        );
        
        // Publish event for OrderService
        RawMaterialReceivedEvent event = new RawMaterialReceivedEvent(
                dto.orderId(), 
                dto.clientId(), 
                dto.sku(), 
                dto.quantity(),
                savedLedger.getLedgerId()
        );
        kafkaTemplate.send("raw_material_received_topic", event);
        log.info("Published RawMaterialReceivedEvent for order: {}", dto.orderId());
    }

    @Override
    public void adjustStock(StockAdjustmentDto dto) {
        log.warn("Adjusting stock for SKU: {}. Reason: {}", dto.sku(), dto.reason());
        InventoryItem item = findItemBySku(dto.sku());
        
        if (dto.quantity().equals(BigDecimal.ZERO)) {
            throw new BusinessLogicException("Adjustment quantity cannot be zero.");
        }
        
        addLedgerEntry(
                item, 
                null, // No order ID for adjustments
                null, // No client ID
                dto.quantity(), 
                TransactionType.ADJUSTMENT
        );
    }

    // --- Private Helper Methods ---

    /**
     * Finds an InventoryItem by SKU or throws a standardized exception.
     */
    protected InventoryItem findItemBySku(String sku) {
        return itemRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryItem", "sku", sku));
    }

    /**
     * Centralized method to create and save a new ledger entry.
     * This is the heart of the service.
     */
    protected InventoryLedger addLedgerEntry(InventoryItem item, Long orderId, Long clientId, BigDecimal quantity, TransactionType type) {
        InventoryLedger entry = new InventoryLedger();
        entry.setItemId(item.getItemId());
        entry.setOrderId(orderId);
        entry.setClientId(clientId);
        entry.setQuantity(quantity);
        entry.setTransactionType(type);
        
        log.info("Adding ledger entry: ItemID={}, Qty={}, Type={}", item.getItemId(), quantity, type);
        return ledgerRepository.save(entry);
    }
}
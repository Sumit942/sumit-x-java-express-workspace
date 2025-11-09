package com.mytextile.inventory.mapper;

import com.mytextile.inventory.dto.CreateItemDto;
import com.mytextile.inventory.dto.ItemDto;
import com.mytextile.inventory.model.InventoryItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    ItemDto toDto(InventoryItem item);

    InventoryItem toEntity(CreateItemDto dto);
}
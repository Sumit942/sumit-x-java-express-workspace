package com.mytextile.orders.mapper;

import com.mytextile.orders.dto.ClientOrderDto;
import com.mytextile.orders.entity.ClientOrder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ClientOrderMapper {

    public ClientOrderDto toDto(ClientOrder clientOrder) {
        if (Objects.isNull(clientOrder)) {
            return null;
        }

        return new ClientOrderDto(
                clientOrder.getOrderId(),
                clientOrder.getClientId(),
                clientOrder.getOrderDescription(),
                clientOrder.getStatus()
        );
    }

    public ClientOrder toEntity(ClientOrderDto dto) {
        if (Objects.isNull(dto)) {
            return null;
        }

        ClientOrder clientOrder = new ClientOrder();
        clientOrder.setClientId(dto.clientId());
        clientOrder.setOrderDescription(dto.orderDescription());
        clientOrder.setStatus(dto.status());
    }
}

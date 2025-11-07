package com.mytextile.orders.repository;

import com.mytextile.orders.entity.ClientOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {
}

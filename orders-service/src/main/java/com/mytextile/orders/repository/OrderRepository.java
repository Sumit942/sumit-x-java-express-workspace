package com.mytextile.orders.repository;

import com.mytextile.orders.model.ClientOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<ClientOrder, Long> {

    List<ClientOrder> findByClientId(Long clientId);
}
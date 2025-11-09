package com.mytextile.dispatch.repository;

import com.mytextile.dispatch.model.DispatchableStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DispatchableStockRepository extends JpaRepository<DispatchableStock, Long> {

    Optional<DispatchableStock> findByOrderIdAndItemSku(Long orderId, String itemSku);
}
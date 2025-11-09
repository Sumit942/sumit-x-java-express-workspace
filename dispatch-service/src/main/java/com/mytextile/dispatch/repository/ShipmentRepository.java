package com.mytextile.dispatch.repository;

import com.mytextile.dispatch.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}

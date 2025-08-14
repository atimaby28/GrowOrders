package org.example.groworders.domain.inventories.repository;

import org.example.groworders.domain.inventories.model.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
}

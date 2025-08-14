package org.example.groworders.domain.orders.repository;

import org.example.groworders.domain.orders.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    //List<Order> findByFarmName(String farmName);
}

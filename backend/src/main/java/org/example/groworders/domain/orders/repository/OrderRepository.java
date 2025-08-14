package org.example.groworders.domain.orders.repository;

import org.example.groworders.domain.orders.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    //List<Order> findByFarmName(String farmName);

    // 농장 이름으로 검색
    List<Order> findByFarmOrder_Name(String farmName);

    // 작물 이름으로 검색
    List<Order> findByCropOrder_CropName(String cropName);

    // 농장 이름 또는 작물 이름으로 검색
    List<Order> findByFarmOrder_NameOrCropOrder_CropName(String farmName, String cropName);
}

package org.example.groworders.domain.orders.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.groworders.domain.orders.model.dto.OrderDto;
import org.example.groworders.domain.orders.model.entity.Order;
import org.example.groworders.domain.orders.model.entity.ShippingStatus;
import org.example.groworders.domain.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;


    public void register(OrderDto.Register dto) {
        orderRepository.save(dto.toEntity());
    }

//    @Transactional
//    public void modify(OrderDto.Modify dto) {
//        Order order = orderRepository.findById(dto.getId()).get();
//
//        dto.updateEntity(order);
//
//
//    }

    public List<OrderDto.OrderResBuyer> listBuyer() {
        List<Order> orderListBuyer = orderRepository.findAll();

        return orderListBuyer.stream().map(OrderDto.OrderResBuyer::from).toList();
    }

    public OrderDto.Register readBuyer(Long id) {
        Optional<Order> result = orderRepository.findById(id);

        if (result.isPresent()) {
            Order entity = result.get();

            return OrderDto.Register.from(entity);
        }
        return null;
    }

    public OrderDto.Modify readFarmer (Long id) {
        Optional<Order> result = orderRepository.findById(id);

        if (result.isPresent()) {
            Order entity = result.get();

            return OrderDto.Modify.from(entity);
        }
        return null;
    }

    //    public List<OrderDto.OrderResBuyer> search(String farmName) {
//        List<Order> result  = orderRepository.findByFarmName(farmName);
//
//        return result.stream().map(OrderDto.OrderResBuyer::from).toList();
//    }
    public List<OrderDto.OrderResBuyer> searchBuyer (String farmName, String cropName) {
        // farmName, cropName 둘 다 null이면 빈 리스트 반환
        if (farmName == null && cropName == null) {
            return List.of();
        }

        List<Order> result = orderRepository.findByFarmOrder_NameOrCropOrder_CropName(farmName, cropName);
        return result.stream()
                .map(OrderDto.OrderResBuyer::from)
                .toList();
    }

    public List<OrderDto.OrderResFarmer> searchFarmer(String cropName, ShippingStatus shippingStatus) {
        // 둘 다 null 이면 빈 리스트 반환
        if (cropName == null && shippingStatus == null) {
            return List.of();
        }

        List<Order> result = orderRepository.findByShippingStatusOrCropOrder_CropName(shippingStatus, cropName);

        return result.stream()
                .map(OrderDto.OrderResFarmer::from)
                .toList();
    }





    public void updateOrder(Long id, OrderDto.Modify dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));


        order.setQuantity(dto.getQuantity());
        //order.setContent(dto.getContent());

        orderRepository.save(order);
    }

    public List<OrderDto.OrderResFarmer> listFarmer() {
        List<Order> orderListFarmer = orderRepository.findAll();

        return orderListFarmer.stream().map(OrderDto.OrderResFarmer::from).toList();
    }

}
package org.example.groworders.domain.orders.service;

import lombok.RequiredArgsConstructor;
import org.example.groworders.domain.orders.model.dto.OrderDto;
import org.example.groworders.domain.orders.model.entity.Order;
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

//    public void modify(OrderDto.Modify dto) {
//        Optional<Order> result = orderRepository.findById(dto.getId());
//    }

    public List<OrderDto.OrderRes> list(){
        List<Order> orderList = orderRepository.findAll();

        return orderList.stream().map(OrderDto.OrderRes::from).toList();
    }

    public OrderDto.OrderRes read(Integer id) {
        Optional<Order> result = orderRepository.findById(id);

        if(result.isPresent()) {
            Order entity = result.get();

            return OrderDto.OrderRes.from(entity);
        }
        return null;
    }

//    public List<OrderDto.OrderRes> search(String farmName) {
//        List<Order> result  = orderRepository.findByFarmName(farmName);
//
//        return result.stream().map(OrderDto.OrderRes::from).toList();
//    }
}

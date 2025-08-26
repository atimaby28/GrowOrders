package org.example.groworders.domain.orders.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.groworders.common.exception.OrderNotFoundException;
import org.example.groworders.domain.orders.model.dto.OrderDto;
import org.example.groworders.domain.orders.model.entity.Order;
import org.example.groworders.domain.orders.model.entity.ShippingStatus;
import org.example.groworders.domain.orders.repository.OrderRepository;
import org.example.groworders.domain.users.model.dto.UserDto;
import org.example.groworders.domain.users.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    //주문생성
    public void register(UserDto.AuthUser authUser,OrderDto.Register dto) {

        orderRepository.save(dto.toEntity(authUser));//고쳐야됨
    }


    //구매자가 주문관리조회
    public OrderDto.OrderList<OrderDto.OrderResBuyer> listBuyer(UserDto.AuthUser authUser, int page, int size) {
        Page<Order> orders = orderRepository.findAll(PageRequest.of(page, size));
        return OrderDto.OrderList.from(orders, OrderDto.OrderResBuyer::from);
    }
    //구매자가 주문관리에서 상세페이지로
    public OrderDto.Register readBuyer(UserDto.AuthUser authUser, Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("해당 주문을 찾을 수 없습니다."));
        return OrderDto.Register.from(order);
    }

//    public OrderDto.Register readBuyer(Long id) {
//        Optional<Order> result = orderRepository.findById(id);
//
//        if (result.isPresent()) {
//            Order entity = result.get();
//
//            return OrderDto.Register.from(entity);
//        }
//        return null;
//    }


    //판매자가 주문관리에서 상세페이지로
    public OrderDto.Modify readFarmer(UserDto.AuthUser authUser, Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("해당 주문을 찾을 수 없습니다."));
        return OrderDto.Modify.from(order);
    }

//    public OrderDto.Modify readFarmer (Long id) {
//        Optional<Order> result = orderRepository.findById(id);
//
//        if (result.isPresent()) {
//            Order entity = result.get();
//
//            return OrderDto.Modify.from(entity);
//        }
//        return null;
//    }

    //구매자 주문관리에서 검색가능
    public List<OrderDto.OrderResBuyer> searchBuyer (UserDto.AuthUser authUser,String farmName, String cropName) {
        // farmName, cropName 둘 다 null이면 빈 리스트 반환
        if (farmName == null && cropName == null) {
            return List.of();
        }

        List<Order> result = orderRepository.findByFarmOrder_NameOrCropOrder_CropName(farmName, cropName);
        return result.stream()
                .map(OrderDto.OrderResBuyer::from)
                .toList();
    }
    //주문관리에서 판매자(농부) 검색가능
    public List<OrderDto.OrderResFarmer> searchFarmer(UserDto.AuthUser authUser, String cropName, ShippingStatus shippingStatus) {
        // 둘 다 null 이면 빈 리스트 반환
        if (cropName == null && shippingStatus == null) {
            return List.of();
        }

        List<Order> result = orderRepository.findByShippingStatusOrCropOrder_CropName(shippingStatus, cropName);

        return result.stream()
                .map(OrderDto.OrderResFarmer::from)
                .toList();
    }




    //주문수정완료
    public void updateOrder(UserDto.AuthUser authUser, Long id, OrderDto.Modify dto) {//고쳐야됨
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("해당 주문을 찾을 수 없습니다."));

        Order updatedOrder = dto.updateEntity(order);
        orderRepository.save(updatedOrder);
    }

//    public void updateOrder(Long id, OrderDto.Modify dto) {
//        Order order = orderRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));
//        Order updatedOrder = dto.updateEntity(order);  // Builder로 새 객체 생성
//        orderRepository.save(updatedOrder);
//    }
    //판매자(농부) 주문관리조회
    public OrderDto.OrderList<OrderDto.OrderResFarmer> listFarmer(UserDto.AuthUser authUser, int page, int size) {
        Page<Order> orders = orderRepository.findByFarmOrder_Id(authUser.getId(), PageRequest.of(page, size));
        return OrderDto.OrderList.from(orders, OrderDto.OrderResFarmer::from);
    }

}
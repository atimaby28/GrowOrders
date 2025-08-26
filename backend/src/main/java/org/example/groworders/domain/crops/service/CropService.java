//package org.example.groworders.domain.crops.service;
//
//import lombok.RequiredArgsConstructor;
//import org.example.groworders.domain.crops.repository.CropRepository;
//import org.example.groworders.domain.orders.model.dto.OrderDto;
//import org.example.groworders.domain.orders.repository.OrderRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CropService {
//    private final CropRepository cropRepository;
//
//
//    public void register(OrderDto.Register dto) {
//        cropRepository.save(dto.toEntity());
//    }
//
//}
package org.example.groworders.domain.orders.controller;


import lombok.RequiredArgsConstructor;
import org.example.groworders.domain.orders.model.dto.OrderDto;
import org.example.groworders.domain.orders.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Transactional
    @PostMapping("/register")//주무생성
    public ResponseEntity register(@RequestBody OrderDto.Register dto){
        orderService.register(dto);

        return ResponseEntity.status(200).body("주문생성성공");
    }

//    @PostMapping("/modify")//주문수정
//    public ResponseEntity modify(@RequestBody OrderDto.Modify dto){
//        orderService.modify(dto);
//
//        return ResponseEntity.status(200).body("주문수정성공");
//    }

    @GetMapping("/list")//주문관리
    public ResponseEntity list(){
        List<OrderDto.OrderRes> response =  orderService.list();

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/read")//주문상세
    public ResponseEntity read(Integer idx){
        OrderDto.OrderRes response = orderService.read(idx);

        return ResponseEntity.status(200).body(response);
    }

//    @GetMapping("/search")-주문검색
//    public ResponseEntity search(String farmName){
//        List<OrderDto.OrderRes> resposnse = orderService.search(farmName);
//
//        return ResponseEntity.status(200).body(resposnse);
//    }

}

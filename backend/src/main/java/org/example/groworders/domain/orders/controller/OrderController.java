package org.example.groworders.domain.orders.controller;


import lombok.RequiredArgsConstructor;
import org.example.groworders.domain.orders.model.dto.OrderDto;
import org.example.groworders.domain.orders.model.entity.ShippingStatus;
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
    public ResponseEntity register(@RequestBody OrderDto.Register dto) {
        orderService.register(dto);

        return ResponseEntity.status(200).body("주문생성성공");
    }

    @PutMapping("/modify/{id}")//주문수정
    public ResponseEntity<String> modify(@PathVariable Long id, @RequestBody OrderDto.Modify dto) {
        orderService.updateOrder(id, dto);
        return ResponseEntity.status(200).body("주문수정성공");
    }

    @GetMapping("/listBuyer")//주문관리-구매자
    public ResponseEntity listBuyer() {
        List<OrderDto.OrderResBuyer> response = orderService.listBuyer();

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/readCreate")//주문생성상세
    public ResponseEntity readBuyer(Long id) {
        OrderDto.Register response = orderService.readBuyer(id);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/readModify")//주문수정상세
    public ResponseEntity readFarmer(Long id) {
        OrderDto.Modify response = orderService.readFarmer(id);

        return ResponseEntity.status(200).body(response);
    }

    //    @GetMapping("/searchBuyer")//주문검색-구매자
//    public ResponseEntity search(String farmName){
//        List<OrderDto.OrderResBuyer> resposnse = orderService.search(farmName);
//
//        return ResponseEntity.status(200).body(resposnse);
//    }
    @GetMapping("/searchBuyer")//주문검색-구매자
    public ResponseEntity<List<OrderDto.OrderResBuyer>> searchBuyer(
            @RequestParam(required = false) String farmName,
            @RequestParam(required = false) String cropName) {

        List<OrderDto.OrderResBuyer> response = orderService.searchBuyer(farmName, cropName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/searchFarmer") // 주문검색 - 판매자
    public ResponseEntity<List<OrderDto.OrderResFarmer>> searchFarmer(
            @RequestParam(required = false) String cropName,
            @RequestParam(required = false) ShippingStatus shippingStatus) {

        List<OrderDto.OrderResFarmer> response = orderService.searchFarmer(cropName, shippingStatus);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/listFarmer")//주문관리-농부
    public ResponseEntity listFarmer(){
        List<OrderDto.OrderResFarmer> response =  orderService.listFarmer();

        return ResponseEntity.status(200).body(response);
    }

    
}

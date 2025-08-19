package org.example.groworders.domain.orders.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "주문 관련 기능")
public class OrderController {
    private final OrderService orderService;


    @Operation(
            summary = "주문생성 - 구매자만 가능",
            description = "주문 생성 버튼 눌렀을 때  주문 생성 내용 DB에 저장"
    )

    @Transactional
    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody OrderDto.Register dto) {
        orderService.register(dto);

        return ResponseEntity.status(200).body("주문생성성공");
    }

    @Operation(
            summary = "주문수정 - 구매자만 가능",
            description = "구매자 주문관리(id 필요)에서 주문 수정 버튼 눌렀을때 주문 수정 내용 DB에 저장"
    )
    @PutMapping("/modify/{id}")
    public ResponseEntity<String> modify(@PathVariable Long id, @Valid @RequestBody OrderDto.Modify dto) {
        orderService.updateOrder(id, dto);
        return ResponseEntity.status(200).body("주문수정성공");
    }

    @Operation(
            summary = "주문관리조회 - 구매자가 보는 화면(농부로 구별 가능)- - 페이징 기능 없음",
            description =  "내가 주문했던 주문목록 조회할 때 전체 주문했던기록을 불러오는 기능"
    )
    @GetMapping("/listBuyer")
    public ResponseEntity listBuyer() {
        List<OrderDto.OrderResBuyer> response = orderService.listBuyer();

        return ResponseEntity.status(200).body(response);
    }

    @Operation(
            summary = "주문생성상세 - 주문생성페이지랑 주문생성상세페이지가 같음",
            description =  "주문관리에서 버튼 눌렀을때 상세 페이지로 가서 주문생성정보들 불러오는 기능 "
    )
    @GetMapping("/readCreate")
    public ResponseEntity readBuyer(Long id) {
        OrderDto.Register response = orderService.readBuyer(id);

        return ResponseEntity.status(200).body(response);
    }

    @Operation(
            summary = "주문수정상세 - 주문수정페이지랑 주문수정상세페이지가 같음",
            description =  "주문관리에서 버튼 눌렀을때 상세 페이지로 가서 주문수정정보들 불러오는 기능 "
    )
    @GetMapping("/readModify")
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

    @Operation(
            summary = "주문검색 - 구매자가 보는 화면(농장이름, 작물이름 검색가능)",
            description =  "주문관리에서 검색했을때 해당 검색내용을 포함한 내용만 불러오는 기능 "
    )
    @GetMapping("/searchBuyer")//주문검색-구매자
    public ResponseEntity<List<OrderDto.OrderResBuyer>> searchBuyer(
            @RequestParam(required = false) String farmName,
            @RequestParam(required = false) String cropName) {

        List<OrderDto.OrderResBuyer> response = orderService.searchBuyer(farmName, cropName);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "주문검색 - 판매자(농부)가 보는 화면(주문상태, 작물이름 검색가능)",
            description =  "주문관리에서 검색했을때 해당 검색내용을 포함한 내용만 불러오는 기능 "
    )
    @GetMapping("/searchFarmer") // 주문검색 - 판매자
    public ResponseEntity<List<OrderDto.OrderResFarmer>> searchFarmer(
            @RequestParam(required = false) String cropName,
            @RequestParam(required = false) ShippingStatus shippingStatus) {

        List<OrderDto.OrderResFarmer> response = orderService.searchFarmer(cropName, shippingStatus);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "주문관리조회 - 판매자(농부)가 보는 화면(구매자로 구별 가능)- - 페이징 기능 없음",
            description =  "농장에 들어온 모든 주문(목록)을 조회할 때 불러오는 기능"
    )
    @GetMapping("/listFarmer")//주문관리-농부
    public ResponseEntity listFarmer(){
        List<OrderDto.OrderResFarmer> response =  orderService.listFarmer();

        return ResponseEntity.status(200).body(response);
    }

    
}

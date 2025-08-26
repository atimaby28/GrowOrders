//package org.example.groworders.domain.farm.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.example.groworders.domain.crops.service.CropService;
//import org.example.groworders.domain.farm.model.dto.Farmdto;
//import org.example.groworders.domain.farm.service.FarmService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/farm")
//@RequiredArgsConstructor
//public class FarmController {
//    private final FarmService farmService;
//
//    @Transactional
//    @PostMapping("/register")//주무생성
//    public ResponseEntity register(@RequestBody Farmdto.Register dto){
//        farmService.register(dto);
//
//        return ResponseEntity.status(200).body("주문생성성공");
//    }
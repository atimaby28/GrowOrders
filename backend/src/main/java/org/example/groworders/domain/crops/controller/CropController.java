//package org.example.groworders.domain.crops.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.example.groworders.domain.crops.model.dto.Cropdto;
//import org.example.groworders.domain.crops.service.CropService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/crop")
//@RequiredArgsConstructor
//public class CropController {
//    private final CropService cropService;
//
//    @Transactional
//    @PostMapping("/register")//주무생성
//    public ResponseEntity register(@RequestBody Cropdto.Register dto){
//        cropService.register(dto);
//
//        return ResponseEntity.status(200).body("주문생성성공");
//    }
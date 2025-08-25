package org.example.groworders.domain.orders.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @ApiResponse(
            responseCode = "200",
            description = "주문생성성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "성공시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "주문생성실패",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "실패시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    public ResponseEntity register(@Valid @RequestBody OrderDto.Register dto) {
        orderService.register(dto);

        return ResponseEntity.status(200).body("주문생성성공");
    }

    @Operation(
            summary = "주문수정 - 구매자만 가능",
            description = "구매자 주문관리(id 필요)에서 주문 수정 버튼 눌렀을때 주문 수정 내용 DB에 저장"
    )
    @PutMapping("/modify/{id}")
    @ApiResponse(
            responseCode = "200",
            description = "주문수정성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "성공시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "주문수정실패",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "실패시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    public ResponseEntity<String> modify(@PathVariable @Schema(description = "Id값으로 조회", required = true, example = "10111")Long id, @Valid @RequestBody OrderDto.Modify dto) {
        orderService.updateOrder(id, dto);
        return ResponseEntity.status(200).body("주문수정성공");
    }

    @Operation(
            summary = "주문관리조회 - 구매자가 보는 화면(농부로 구별 가능)- - 페이징 기능 있음",
            description =  "내가 주문했던 주문목록 조회할 때 전체 주문했던기록을 불러오는 기능"
    )
    @GetMapping("/listBuyer")
    @ApiResponse(
            responseCode = "200",
            description = "주문조회성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "성공시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "주문조회실패",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "실패시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    public ResponseEntity listBuyer(
            @Schema(description = "페이지 번호, 1번부터 시작", required = true, example = "1") Integer page,
            @Schema(description = "각 페이지당 게시글 수", required = true, example = "10") Integer size) {

        if (page == null || page < 1) page = 1;
        if (size == null || size < 1) size = 10;

        OrderDto.OrderList<OrderDto.OrderResBuyer> response = orderService.listBuyer(page - 1, size);

        return ResponseEntity.status(200).body(response);
    }

    @Operation(
            summary = "주문생성상세 - 주문생성페이지랑 주문생성상세페이지가 같음",
            description =  "주문관리에서 버튼 눌렀을때 상세 페이지로 가서 주문생성정보들 불러오는 기능 "
    )
    @GetMapping("/readCreate")
    @ApiResponse(
            responseCode = "200",
            description = "주문생성상세조회성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "성공시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "주문생성상세조회실패",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "실패시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    public ResponseEntity readBuyer(@Schema(description = "Id값으로 조회", required = true, example = "10111")Long id) {
        OrderDto.Register response = orderService.readBuyer(id);

        return ResponseEntity.status(200).body(response);
    }

    @Operation(
            summary = "주문수정상세 - 주문수정페이지랑 주문수정상세페이지가 같음",
            description =  "주문관리에서 버튼 눌렀을때 상세 페이지로 가서 주문수정정보들 불러오는 기능 "
    )
    @GetMapping("/readModify")
    @ApiResponse(
            responseCode = "200",
            description = "주문수정상세조회성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "성공시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "주문수정상세조회실패",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "실패시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    public ResponseEntity readFarmer(@Schema(description = "Id값으로 조회", required = true, example = "10111")Long id) {
        OrderDto.Modify response = orderService.readFarmer(
                id);

        return ResponseEntity.status(200).body(response);
    }


    @Operation(
            summary = "주문검색 - 구매자가 보는 화면(농장이름, 작물이름 검색가능)",
            description =  "주문관리에서 검색했을때 해당 검색내용을 포함한 내용만 불러오는 기능 "
    )
    @ApiResponse(
            responseCode = "200",
            description = "검색조회성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "성공시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "검색조회실패",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "실패시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    @GetMapping("/searchBuyer")//주문검색-구매자
    public ResponseEntity<List<OrderDto.OrderResBuyer>> searchBuyer(
            @Schema(description = "농장이름", example = "사랑농장") String farmName,
            @Schema(description = "작물이름", example = "토마토") String cropName) {

        List<OrderDto.OrderResBuyer> response = orderService.searchBuyer(farmName, cropName);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "주문검색 - 판매자(농부)가 보는 화면(주문상태, 작물이름 검색가능)",
            description =  "주문관리에서 검색했을때 해당 검색내용을 포함한 내용만 불러오는 기능 "
    )
    @GetMapping("/searchFarmer") // 주문검색 - 판매자
    @ApiResponse(
            responseCode = "200",
            description = "검색조회성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "성공시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "검색조회실패",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "실패시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    public ResponseEntity<List<OrderDto.OrderResFarmer>> searchFarmer(
            @Schema(description = "작물이름", example = "토마토") String cropName,
            @Schema(description = "배송상태", example = "PREPARING") ShippingStatus shippingStatus) {

        List<OrderDto.OrderResFarmer> response = orderService.searchFarmer(cropName, shippingStatus);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "주문관리조회 - 판매자(농부)가 보는 화면(구매자로 구별 가능)- - 페이징 기능 있음",
            description =  "농장에 들어온 모든 주문(목록)을 조회할 때 불러오는 기능"
    )
    @GetMapping("/listFarmer")
    @ApiResponse(
            responseCode = "200",
            description = "주문조회성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "성공시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "주문조회실패",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "실패시 응답",
                            value = """
                    {

                    }
                    """
                    )
            )
    )
    public ResponseEntity listFarmer(
            @Schema(description = "페이지 번호, 1번부터 시작", required = true, example = "1") Integer page,
            @Schema(description = "각 페이지당 게시글 수", required = true, example = "10") Integer size) {

        if (page == null || page < 1) page = 1;
        if (size == null || size < 1) size = 10;

        OrderDto.OrderList<OrderDto.OrderResFarmer> response = orderService.listFarmer(page - 1, size);
        return ResponseEntity.status(200).body(response);
    }

    
}

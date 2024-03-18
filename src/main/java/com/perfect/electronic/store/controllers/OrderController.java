package com.perfect.electronic.store.controllers;

import com.perfect.electronic.store.dtos.ApiResponseMessage;
import com.perfect.electronic.store.dtos.CreateOrderRequest;
import com.perfect.electronic.store.dtos.OrderDto;
import com.perfect.electronic.store.dtos.PageableResponse;
import com.perfect.electronic.store.entities.Order;
import com.perfect.electronic.store.services.OrderService;
import jakarta.validation.Valid;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid CreateOrderRequest orderRequest){
        OrderDto orderDto=orderService.createOrder(orderRequest);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId){
        orderService.removeOrder(orderId);
       ApiResponseMessage response= ApiResponseMessage.builder()
                .status(HttpStatus.OK)
                .message("Order Remove Successfully")
                .success(true)
                .build();
       return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PageableResponse> getAllOrders(
            @RequestParam(value="pageNumber", defaultValue="0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "orderDate",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false)String sortDir
    ){
        PageableResponse orders=orderService.getAllOrders(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getUserOrder(@PathVariable String userId){
        List<OrderDto> orderDTOs=orderService.getAllOrdersOfUser(userId);
        return new ResponseEntity<>(orderDTOs,HttpStatus.OK);
    }
}

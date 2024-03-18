package com.perfect.electronic.store.services;

import com.perfect.electronic.store.dtos.CreateOrderRequest;
import com.perfect.electronic.store.dtos.OrderDto;
import com.perfect.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface OrderService {
    // create Order
    OrderDto createOrder(CreateOrderRequest orderDto);
    //remove Order
    void removeOrder(String orderId);
    //Get All Order OF User
    List<OrderDto> getAllOrdersOfUser(String userId);
    //Admin Get All Order
    PageableResponse<OrderDto> getAllOrders(int pageNumber,int pageSize,String sortBy,String sortDir);
    //Order methods(logic
}

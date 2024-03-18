package com.perfect.electronic.store.dtos;

import com.perfect.electronic.store.entities.OrderItem;
import com.perfect.electronic.store.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class OrderDto {
    private  String orderId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    private int orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date deliverDate;
    private Date orderDate=new Date();
    //user
//    private UserDto user;
    private List<OrderItemDto> orderItems=new ArrayList<>();
}

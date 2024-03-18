package com.perfect.electronic.store.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Order {
    @Id
    private  String orderId;
//    PENDING DISPATCH, DELIVER
    private String orderStatus;
    // NOTPAID, PAID
    //enum
    //boolean false->NOT PAID, True ->Paid
    private String paymentStatus;
    private int orderAmount;
    @Column(length = 10000)
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date deliverDate;
    private Date orderDate;

    //user
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems=new ArrayList<>();
}

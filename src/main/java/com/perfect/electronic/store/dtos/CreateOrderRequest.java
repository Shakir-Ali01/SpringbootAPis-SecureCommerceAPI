package com.perfect.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CreateOrderRequest {

    @NotBlank(message = "Cart Is Required")
    private String cartId;
    @NotBlank(message = "User Id Is Required")
    private String userId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    @NotBlank(message = "Address Is Required")
    private String billingAddress;
    @NotBlank(message = "Billing Phone Is Required")
    private String billingPhone;
    @NotBlank(message = "Billing Name Is Required")
    private String billingName;

}

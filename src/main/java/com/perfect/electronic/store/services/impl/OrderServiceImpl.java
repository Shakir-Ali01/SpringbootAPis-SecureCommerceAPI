package com.perfect.electronic.store.services.impl;

import com.perfect.electronic.store.dtos.CreateOrderRequest;
import com.perfect.electronic.store.dtos.OrderDto;
import com.perfect.electronic.store.dtos.PageableResponse;
import com.perfect.electronic.store.entities.*;
import com.perfect.electronic.store.exceptions.BadApiRequestException;
import com.perfect.electronic.store.exceptions.ResourceNotFoundException;
import com.perfect.electronic.store.helpers.Helper;
import com.perfect.electronic.store.repositories.CartRepository;
import com.perfect.electronic.store.repositories.OrderReposotory;
import com.perfect.electronic.store.repositories.UserRepository;
import com.perfect.electronic.store.services.OrderService;
import jakarta.transaction.Transactional;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderReposotory orderReposotory;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CartRepository cartRepository;
    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        //fetch user
        User user=userRepository.findById(orderDto.getUserId()).orElseThrow(()-> new ResourceNotFoundException("User Not Found With Given ID"));
        //fetch Cart
        Cart cart=cartRepository.findById(orderDto.getCartId()).orElseThrow(()-> new ResourceNotFoundException("Cart Not Found With Given ID"));
        List<CartItem> cartItems=cart.getItems();
        if(cartItems.size() <= 0){
            throw new BadApiRequestException("Invalid Number of Item in cart");
        }
        //other Checks

        //Generate Order
       Order order= Order.builder()
                .billingName(orderDto.getBillingName())
                .billingAddress(orderDto.getBillingAddress())
                .orderDate(new Date())
                .deliverDate(null)
                .billingPhone(orderDto.getBillingPhone())
                .orderStatus(orderDto.getOrderStatus())
                .paymentStatus(orderDto.getPaymentStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();
        //order Items Amount

        //convert Cart Item To OrderItems
        AtomicReference<Integer> orderAmount=new AtomicReference<>(0);
       List<OrderItem> orderItems= cartItems.stream().map(cartItem -> {
            //CartItem->OrderItem
           OrderItem orderItem= OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
           orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
            return  orderItem;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());
        Order savedOrder=orderReposotory.save(order);
        //clear Cart
        cart.getItems().clear();
        cartRepository.save(cart);
        OrderDto ord= modelMapper.map(savedOrder,OrderDto.class);
        return ord;
    }

    @Override
    public void removeOrder(String orderId) {
        Order order=orderReposotory.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order Not Found With Given ID"));
         orderReposotory.delete(order);
    }

    @Override
    public List<OrderDto> getAllOrdersOfUser(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User NOt Found With Given ID"));
        List<Order> orders=orderReposotory.findByUser(user);
       List<OrderDto> orderDTos= orders.stream().map(order -> modelMapper.map(order,OrderDto.class)).collect(Collectors.toList());
        return orderDTos;
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
       Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
//        Sort sort= (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page=orderReposotory.findAll(pageable);
        return Helper.getPageableResponse(page,OrderDto.class);
    }
}

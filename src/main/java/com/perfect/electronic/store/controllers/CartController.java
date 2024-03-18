package com.perfect.electronic.store.controllers;

import com.perfect.electronic.store.dtos.AddItemToCartRequest;
import com.perfect.electronic.store.dtos.ApiResponseMessage;
import com.perfect.electronic.store.dtos.CartDto;
import com.perfect.electronic.store.entities.Cart;
import com.perfect.electronic.store.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@Valid
public class CartController {
    @Autowired
    private  CartService cartService;
//    add items to cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest addItemToCartRequest){
       CartDto cart= cartService.addItemToCart(userId,addItemToCartRequest);
       return  new ResponseEntity<>(cart, HttpStatus.CREATED);
    }
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId,@PathVariable int itemId){
        cartService.removeItemFromCart(userId,itemId);
      ApiResponseMessage response=  ApiResponseMessage.builder()
                .message("Item Is Removed")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId){
        cartService.clearCart(userId);
        ApiResponseMessage response=  ApiResponseMessage.builder()
                .message("Cart is Clear")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId)
    {
        CartDto cartDto=cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto,HttpStatus.OK);
    }

}

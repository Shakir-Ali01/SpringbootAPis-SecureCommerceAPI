package com.perfect.electronic.store.services;

import com.perfect.electronic.store.dtos.AddItemToCartRequest;
import com.perfect.electronic.store.dtos.CartDto;
import com.perfect.electronic.store.entities.CartItem;
import org.springframework.stereotype.Service;


public interface CartService {
//    add item to cart:
//    case 1:- cart for user is not available: we will create the cart then add the item to cart
//    case 2 : cart available add the item to cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove Item From Cart
    void removeItemFromCart(String userId,int cartItem);

    void clearCart(String userId);

    CartDto getCartByUser(String userId);
}

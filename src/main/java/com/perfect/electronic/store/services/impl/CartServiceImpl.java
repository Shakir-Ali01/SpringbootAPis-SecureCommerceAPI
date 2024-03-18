package com.perfect.electronic.store.services.impl;
import com.perfect.electronic.store.dtos.AddItemToCartRequest;
import com.perfect.electronic.store.dtos.CartDto;
import com.perfect.electronic.store.entities.Cart;
import com.perfect.electronic.store.entities.CartItem;
import com.perfect.electronic.store.entities.Product;
import com.perfect.electronic.store.entities.User;
import com.perfect.electronic.store.exceptions.ResourceNotFoundException;
import com.perfect.electronic.store.repositories.CartItemRepository;
import com.perfect.electronic.store.repositories.CartRepository;
import com.perfect.electronic.store.repositories.ProductRepository;
import com.perfect.electronic.store.repositories.UserRepository;
import com.perfect.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        int quantity =request.getQuantity();
        String productId= request.getProductId();
        if(quantity<=0){
            throw new RuntimeException("Requested Quantity  is Not Valid");
        }
        //fetch The Product
        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product Id Not Find With This Id"));
        //fetch the User From DB
       User user= userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Id Not Found With Given ID"));

       //Check user Already have cart OR Not if dont have then create cart
        Cart cart=null;
        try{
            cart=cartRepository.findByUser(user).get();

        }catch (NoSuchElementException ex){
             cart=new Cart();
             cart.setCartId(UUID.randomUUID().toString());
             cart.setCreatedAt(new Date());
        }
//        IF Cart Item Already Present Then Update
        List<CartItem> items=cart.getItems();
//        We can not update variable value in map so will use Atomic Class
//        boolean updated=false;
        AtomicReference<Boolean> updated=new AtomicReference<>(false);
        items=items.stream().map(item->{
            if(item.getProduct().getProductId().equals(productId)){
//                Item Already Present In Cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

//       cart.setItems(items);
        // If Not Present then create Items
        if(!updated.get()){
            CartItem cartItem=CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity*product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }
       // Perform Cart Operation

        //create ITems

        cart.setUser(user);
        Cart updateCart=cartRepository.save(cart);
        return mapper.map(updateCart,CartDto.class);

    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        //condition

      CartItem cartItem1=cartItemRepository.findById(cartItem).orElseThrow(()->new ResourceNotFoundException("Cart Item Not Found In DB"));

      cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
    User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not Found With Given ID"));
    Cart cart=cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("Cart OF Given User Is Not Found"));
    cart.getItems().clear();
    cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not Found With Given ID"));
        Cart cart=cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("Cart OF Given User Is Not Found"));
        return mapper.map(cart,CartDto.class);
    }
}

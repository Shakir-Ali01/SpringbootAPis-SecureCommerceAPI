package com.perfect.electronic.store.repositories;

import com.perfect.electronic.store.entities.Cart;
import com.perfect.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {
    Optional<Cart> findByUser(User user);
}

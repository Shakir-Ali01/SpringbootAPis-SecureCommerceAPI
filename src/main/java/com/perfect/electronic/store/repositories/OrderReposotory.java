package com.perfect.electronic.store.repositories;

import com.perfect.electronic.store.entities.Order;
import com.perfect.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderReposotory extends JpaRepository<Order,String> {
    List<Order> findByUser(User user);

}

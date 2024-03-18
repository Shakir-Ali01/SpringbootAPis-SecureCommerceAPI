package com.perfect.electronic.store.repositories;

import com.perfect.electronic.store.entities.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryRepository extends JpaRepository<Category,String> {
    List<Category> findByTitleContaining(String keyword);
}

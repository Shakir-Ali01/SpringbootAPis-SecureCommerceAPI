package com.perfect.electronic.store.repositories;

import com.perfect.electronic.store.entities.Category;
import com.perfect.electronic.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,String> {
    //search
    Page<Product> findByTitleContaining(Pageable pageable,String subTitle);
//    List<Product> findByLive(boolean live);
    Page<Product> findByLiveTrue(Pageable pageable);
    Page<Product> findByCategory(Category category, Pageable pageable );

}

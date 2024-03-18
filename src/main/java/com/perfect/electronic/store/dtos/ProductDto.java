package com.perfect.electronic.store.dtos;

import com.perfect.electronic.store.entities.Category;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDto {
    private String productId;
    private String title;

    private String description;
    private int price;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private int discountedPrice;
    private String productImageName;
    private CategoryDto category;
}

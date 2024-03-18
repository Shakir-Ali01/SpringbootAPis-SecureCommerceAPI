package com.perfect.electronic.store.services;

import com.perfect.electronic.store.dtos.PageableResponse;
import com.perfect.electronic.store.dtos.ProductDto;

import java.util.List;

public interface ProductService {
     //create
    ProductDto create(ProductDto productDto);
     //update
    ProductDto update(ProductDto productDto,String productId);
    //get all product
   PageableResponse<ProductDto> getAllProduct(int pageNumber,int pageSize, String sortBy, String sortDir);
    //get Single product
    ProductDto getSingleProduct(String productId);
    //delete product
    void delete(String productId);
    //search product
     PageableResponse<ProductDto> searchProductByTitle(int pageNumber,int pageSize, String sortBy, String sortDir,String subTitle);
    //get All Live Product
    PageableResponse<ProductDto> getAllLiveProduct(int pageNumber,int pageSize, String sortBy, String sortDir);
    //create product with Category
    ProductDto createProductWithCategory(ProductDto productDto,String categoryId);
// Update Category of Product
    ProductDto updateCategory(String productId,String categoryId);
    //Get All Product of Partucular Category
    PageableResponse<ProductDto> getAllProductsOfParticularCategory(String categoryId,int pageSize,int pageNumber,String  sortBy,String sortDir);
}

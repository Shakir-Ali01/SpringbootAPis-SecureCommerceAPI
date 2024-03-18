package com.perfect.electronic.store.controllers;

import com.perfect.electronic.store.dtos.*;
import com.perfect.electronic.store.services.CategoryService;
import com.perfect.electronic.store.services.FileService;
import com.perfect.electronic.store.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Validated
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;
    //create
    @PostMapping
      public ResponseEntity<CategoryDto> create(@RequestBody  @Valid CategoryDto categoryDto){
          CategoryDto categoryDto1=categoryService.create(categoryDto);
          return  new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
      }
    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> update(@RequestBody CategoryDto categoryDto,@PathVariable String categoryId)
    {
        CategoryDto categoryDto1=categoryService.update(categoryDto,categoryId);
        return new ResponseEntity<>(categoryDto1,HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String categoryId)
    {
        categoryService.delete(categoryId);
        ApiResponseMessage response=new ApiResponseMessage().builder()
                .message("category deleted successfully")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    //get all category
    @GetMapping
    public ResponseEntity<PageableResponse> getAllCategory(@RequestParam(value="pageNumber" ,defaultValue = "0",required = false)int pageNumber,
                                                           @RequestParam(value="pageSize",defaultValue = "10",required = false)int pageSize,
                                                           @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
                                                           @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir){
        PageableResponse<CategoryDto> pageableResponse= categoryService.getAll(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }
    //get Single Category
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId)
    {
       CategoryDto categoryDto= categoryService.get(categoryId);
//       This will get response ok automatically
       return ResponseEntity.ok(categoryDto);
    }
    //search
    @GetMapping("search/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchUser(@PathVariable String keyword){
        return new ResponseEntity<>(categoryService.searchUser(keyword),HttpStatus.OK);
    }
//    Create Product With Category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categoryId,@RequestBody @Valid ProductDto productDto)
    {
        ProductDto productWithCategory= productService.createProductWithCategory(productDto,categoryId);
        return  new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);
    }
    //update Category of Product
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateProductCategory(@PathVariable String categoryId,@PathVariable String productId){
       ProductDto productDto= productService.updateCategory(productId,categoryId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }
//    Get All Products Of Particular Category
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProductOfParticularCategoryId(
            @PathVariable String categoryId,
            @RequestParam(value="pageNumber" ,defaultValue = "0",required = false)int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false)int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir){
        PageableResponse<ProductDto> productDto=productService.getAllProductsOfParticularCategory(categoryId,pageSize,pageNumber,sortBy,sortDir);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }
}

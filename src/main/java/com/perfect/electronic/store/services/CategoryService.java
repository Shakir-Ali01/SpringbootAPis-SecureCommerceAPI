package com.perfect.electronic.store.services;

import com.perfect.electronic.store.dtos.CategoryDto;
import com.perfect.electronic.store.dtos.PageableResponse;
import com.perfect.electronic.store.dtos.UserDto;
import com.perfect.electronic.store.entities.Category;

import java.util.List;

public interface CategoryService {
    //create
    CategoryDto create(CategoryDto categoryDto);
    //update
    CategoryDto update(CategoryDto categoryDto,String categoryId);
    //delete
    void delete(String categoryId);
    //getAll
    PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);
    // get Single Category
     CategoryDto get(String categoryId);
    //search
     List<CategoryDto> searchUser(String keyword);
}

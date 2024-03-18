package com.perfect.electronic.store.services.impl;
import com.perfect.electronic.store.dtos.CategoryDto;
import com.perfect.electronic.store.dtos.PageableResponse;
import com.perfect.electronic.store.dtos.UserDto;
import com.perfect.electronic.store.entities.Category;
import com.perfect.electronic.store.entities.User;
import com.perfect.electronic.store.exceptions.ResourceNotFoundException;
import com.perfect.electronic.store.helpers.Helper;
import com.perfect.electronic.store.repositories.CategoryRepository;
import com.perfect.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        Category category=mapper.map(categoryDto, Category.class);
        //creating category ID Randomly
        String categoryId= UUID.randomUUID().toString();
        category.setCategoryId(categoryId);
        Category savedCategory=categoryRepository.save(category);
        //convert Entity to DTO
        CategoryDto categoryDto1=mapper.map(category,CategoryDto.class);
        return categoryDto1;
    }
    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        //get category of given id
//        Category categoryByUserId=categoryRepository.findById(categoryId).get();
        Category categoryByUserId=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category Not Found with given Id"));
        //convert DTO TO Entity
        Category getCategoryFromClient=mapper.map(categoryDto,Category.class);
        //update category
        categoryByUserId.setDescription(getCategoryFromClient.getDescription());
        categoryByUserId.setTitle(getCategoryFromClient.getTitle());
        categoryByUserId.setCoverImage(getCategoryFromClient.getCoverImage());
        //update data
       Category updatedCategory= categoryRepository.save(categoryByUserId);
       return  mapper.map(updatedCategory,CategoryDto.class);
    }
    @Override
    public void delete(String categoryId) {
        categoryRepository.deleteById(categoryId);
    }
    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page=categoryRepository.findAll(pageable);
        System.out.println("Page Values"+ page.get());
        PageableResponse<CategoryDto> pageableResponse= Helper.getPageableResponse(page,CategoryDto.class);
        return pageableResponse;
    }
    @Override
    public CategoryDto get(String categoryId) {
       Category category= categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("This category Id not Exists"));
        return mapper.map(category,CategoryDto.class);
    }
    @Override
    public List<CategoryDto> searchUser(String keyword) {
        List<Category> category=categoryRepository.findByTitleContaining(keyword);
        List<CategoryDto> dtoList= category.stream().map(category1->mapper.map(category1,CategoryDto.class)).collect(Collectors.toList());
//      List<CategoryDto> dtoList= category.stream().map(object->new ModelMapper().map(object,CategoryDto.class)).collect(Collectors.toList());
        return dtoList;
    }
}

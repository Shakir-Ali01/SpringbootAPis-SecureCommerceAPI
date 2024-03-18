package com.perfect.electronic.store.services.impl;

import com.perfect.electronic.store.dtos.CategoryDto;
import com.perfect.electronic.store.dtos.PageableResponse;
import com.perfect.electronic.store.dtos.ProductDto;
import com.perfect.electronic.store.entities.Category;
import com.perfect.electronic.store.entities.Product;
import com.perfect.electronic.store.exceptions.ResourceNotFoundException;
import com.perfect.electronic.store.helpers.Helper;
import com.perfect.electronic.store.repositories.CategoryRepository;
import com.perfect.electronic.store.repositories.ProductRepository;
import com.perfect.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${product.image.path}")
    private String imagePath;
    @Autowired
    private CategoryRepository categoryRepository;
    Logger logger= LoggerFactory.getLogger(ProductService.class);
    @Override
    public ProductDto create(ProductDto productDto) {
//        Genetate product Id
       String productId= UUID.randomUUID().toString();
       productDto.setProductId(productId);
       //set Added date
        productDto.setAddedDate(new Date());
//        convert DTO TO Entity
        Product product=mapper.map(productDto,Product.class);
        Product savedProduct=productRepository.save(product);
        //convert entity to Dto and return to API
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        //Fetch the Product using given ID
        Product productEntity=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product Not Found with Given ID"));
        //Set Product DTO Values To Entity TO Update The value
        productEntity.setTitle(productDto.getTitle());
        productEntity.setLive(productDto.isLive());
        productEntity.setPrice(productDto.getPrice());
        productEntity.setDescription(productDto.getDescription());
        productEntity.setDiscountedPrice(productDto.getDiscountedPrice());
        productEntity.setQuantity(productDto.getQuantity());
        productEntity.setStock(productDto.isStock());
        productEntity.setProductImageName(productDto.getProductImageName());
        //Update Entity into DB
        Product updatedProduct=productRepository.save(productEntity);
        //Convert Entity To DTO and Return to Api
        return mapper.map(updatedProduct,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProduct(int pageNumber,int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findAll(pageable);
        PageableResponse<ProductDto> pageableResponse= Helper.getPageableResponse(page,ProductDto.class);
        return pageableResponse;

    }

    @Override
    public ProductDto getSingleProduct(String productId) {
        //Fetch the Product using given ID
        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product Not Found with Given ID"));
        //Convert Entity To DTO And Return To API
        return mapper.map(product,ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        //Fetch the Product using given ID
        Product productEntity=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product Not Found with Given ID"));
        //delete User Profile Image
        String fullPath = imagePath + productEntity.getProductImageName();
        logger.info("Product Image Full Path {}",fullPath);
        try{
            Path path= Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            logger.info("Product Image NOt Found In Image folder");
            ex.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        productRepository.delete(productEntity);
    }

    @Override
    public PageableResponse<ProductDto> searchProductByTitle(int pageNumber,int pageSize, String sortBy, String sortDir,String subTitle) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findByTitleContaining(pageable,subTitle);
        PageableResponse<ProductDto> pageableResponse= Helper.getPageableResponse(page,ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> getAllLiveProduct(int pageNumber,int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findByLiveTrue(pageable);
        PageableResponse<ProductDto> pageableResponse= Helper.getPageableResponse(page,ProductDto.class);
        return pageableResponse;
    }

    @Override
    public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {
//       Fetch The Category from DB
         Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("CateGory Not Found With Given Category ID"));
//         Save Product with Category in Product Table


//        Genetate product Id
        String productId= UUID.randomUUID().toString();
        productDto.setProductId(productId);
        //set Added date
        productDto.setAddedDate(new Date());
        //Set Category in productEntity

//        convert DTO TO Entity
        //      Convert Product DTO Into Entity
        Product product=mapper.map(productDto,Product.class);
        product.setCategory(category);
        Product savedProduct=productRepository.save(product);
        //convert entity to Dto and return to API
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
        //Product Fetch
        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("PRoduct Id Not find With Given ID"));
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category Not Found With Given ID"));
        product.setCategory(category);
       Product savedProduct= productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProductsOfParticularCategory(String categoryId,int pageSize,int pageNumber,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category Not Found With Given ID"));
       Page<Product> page= productRepository.findByCategory(category,pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }

}

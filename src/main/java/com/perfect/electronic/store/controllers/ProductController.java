package com.perfect.electronic.store.controllers;

import com.perfect.electronic.store.dtos.*;
import com.perfect.electronic.store.services.FileService;
import com.perfect.electronic.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("product")
@Validated
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;
    @Value("${product.image.path}")
    private String imagePath;
    //create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid  ProductDto productDto){
        ProductDto createdProduct=productService.create(productDto);
        return  new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid  ProductDto productDto,@PathVariable String productId){
        ProductDto updatedProduct=productService.update(productDto,productId);
        return  new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId){
        productService.delete(productId);
       ApiResponseMessage responseMessage= ApiResponseMessage.builder().message("Product is Deleted Successfully")
                .status(HttpStatus.OK)
                .success(true).build();
       return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }
    //get All Product
    //Get Single Product
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId)
    {
        ProductDto product=productService.getSingleProduct(productId);
        return  new ResponseEntity<>(product,HttpStatus.OK);
    }
    //Get All Live Product
    @GetMapping("/liveProduct")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProduct(@RequestParam(value="pageNumber", defaultValue="0",required = false) int pageNumber,
                                                          @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
                                                          @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
                                                          @RequestParam(value="sortDir",defaultValue = "asc",required = false)String sortDir){
        PageableResponse<ProductDto> productList=productService.getAllLiveProduct(pageNumber,pageSize,sortBy,sortDir);
        return  new ResponseEntity<>(productList,HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(@RequestParam(value="pageNumber", defaultValue="0",required = false) int pageNumber,
                                                          @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
                                                          @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
                                                          @RequestParam(value="sortDir",defaultValue = "asc",required = false)String sortDir){
       PageableResponse<ProductDto> productList=productService.getAllProduct(pageNumber,pageSize,sortBy,sortDir);
       return  new ResponseEntity<>(productList,HttpStatus.OK);
    }
    //Search Product
    @GetMapping("search/{keyword}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(@RequestParam(value="pageNumber", defaultValue="0",required = false) int pageNumber,
                                                          @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
                                                          @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
                                                          @RequestParam(value="sortDir",defaultValue = "asc",required = false)String sortDir,
                                                          @PathVariable String keyword)
    {
        PageableResponse<ProductDto> productList=productService.searchProductByTitle(pageNumber,pageSize,sortBy,sortDir,keyword);
        return  new ResponseEntity<>(productList,HttpStatus.OK);
    }
    //Upload Image
    @PostMapping("image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable String productId, @RequestParam("productImage")MultipartFile image) throws IOException {
        String fileName=fileService.uploadFile(image,imagePath);
        ProductDto productDto=productService.getSingleProduct(productId);
        productDto.setProductImageName(fileName);
        //update product imageName in DATABASE
        ProductDto updatedProduct=productService.update(productDto,productId);
        //Created Response
       ImageResponse  response= ImageResponse.builder()
                .imageName(updatedProduct.getProductImageName())
                .success(true)
                .message("Product Image Is Successfully Uploaded")
                .status(HttpStatus.CREATED).build();
        return  new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    //Serve Image
    @GetMapping("/image/{productId}")
    public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto product=productService.getSingleProduct(productId);
//        logger.info("User Image Name {}",product.getProductImageName());
        InputStream resources=fileService.getResources(imagePath,product.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resources,response.getOutputStream());
    }
}

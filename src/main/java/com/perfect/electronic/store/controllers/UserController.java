package com.perfect.electronic.store.controllers;
import com.perfect.electronic.store.dtos.ApiResponseMessage;
import com.perfect.electronic.store.dtos.ImageResponse;
import com.perfect.electronic.store.dtos.PageableResponse;
import com.perfect.electronic.store.dtos.UserDto;
import com.perfect.electronic.store.services.FileService;
import com.perfect.electronic.store.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
@RestController
@RequestMapping("/users")
@Validated
@Tag(name = "Users Controller ", description = "APIs for Users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    private Logger logger= LoggerFactory.getLogger(UserController.class);
    @Value("${user.profile.image.path}")
    private String imageUploadPath;
    //create
    @PostMapping("")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto){
        UserDto userDto1=userService.createUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId,@RequestBody @Valid UserDto userDto) {

        UserDto updatedUserDto=userService.updateUser(userDto,userId);
        return new ResponseEntity<>(updatedUserDto,HttpStatus.OK);
    }
    //Delete User
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        ApiResponseMessage msg=ApiResponseMessage.builder()
                .message("User is Deleted Successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return  new ResponseEntity<>(msg,HttpStatus.OK);
    }
    //get all
    @Tag(name = "get", description = "GET methods of user APIs")
    @GetMapping("")
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir)
    {
        System.out.println("Users method called successfully");
        return  new ResponseEntity<>(userService.getAllUser(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }
    //get single

    @Tag(name = "get", description = "GET methods of user APIs ")
    @Operation(summary = "Get an user",
            description = "get  an existing user. The response is get user object with id, first name, and last name. etc..")
@ApiResponses({
        @ApiResponse(responseCode = "200",  description = "Successfull "),
        @ApiResponse(responseCode = "404", description = "User not found"),
                })
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@Parameter(
            description = "ID of user to be retrieved",
            required = true)@PathVariable String userId){
        return new ResponseEntity<>(userService.getSingleUserById(userId),HttpStatus.OK);
    }
    //get By EmailId
    @GetMapping("/email/{emailId}")
    public ResponseEntity<UserDto> getUserByEmailId(@PathVariable String emailId){
        return new ResponseEntity<>(userService.getSingleUserByEmail(emailId),HttpStatus.OK);
    }
    //Search User
    @GetMapping("search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword){
        return new ResponseEntity<>(userService.searchUser(keyword),HttpStatus.OK);
    }
    //upload user Image
    @PostMapping("/image/{userId}")
        public ResponseEntity<ImageResponse> uploadUserImage(@PathVariable String userId,@RequestParam("userImage") MultipartFile image) throws IOException {
        //
        String imageName= fileService.uploadFile(image,imageUploadPath);
         //getting user info using user ID
        UserDto user=userService.getSingleUserById(userId);
        user.setImageName(imageName);
       //updating  image name in database
        UserDto userDto=userService.updateUser(user,userId);
        //uploading image into folder
        ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).status(HttpStatus.CREATED).success(true).build();
        return  new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
        }
    //Serve User Image(Return user image in response)
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
    UserDto user=userService.getSingleUserById(userId);
    logger.info("User Image Name {}",user.getImageName());
    InputStream resources=fileService.getResources(imageUploadPath,user.getImageName());
    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    StreamUtils.copy(resources,response.getOutputStream());
    }
    //Delete User Image
}

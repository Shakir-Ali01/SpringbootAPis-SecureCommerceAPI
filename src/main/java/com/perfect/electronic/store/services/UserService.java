package com.perfect.electronic.store.services;

import com.perfect.electronic.store.dtos.PageableResponse;
import com.perfect.electronic.store.dtos.UserDto;
import com.perfect.electronic.store.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    //create User
    UserDto createUser(UserDto user);

    //update user
    UserDto updateUser(UserDto userDto,String userId);

    //deleter user
     void deleteUser(String userId);

    //get single user by id
    UserDto getSingleUserById(String userId);

    //get single user by Email
    UserDto getSingleUserByEmail(String emailId);


    //get All User
     PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);
    //search User
    List<UserDto> searchUser(String keyword);


    //Other User specific features
    Optional<User> findUserByEmailOptional(String email);

}

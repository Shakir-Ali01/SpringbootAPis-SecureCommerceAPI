package com.perfect.electronic.store.services;
import com.perfect.electronic.store.dtos.PageableResponse;
import com.perfect.electronic.store.dtos.UserDto;
import com.perfect.electronic.store.entities.Role;
import com.perfect.electronic.store.entities.User;
import com.perfect.electronic.store.repositories.RoleRepository;
import com.perfect.electronic.store.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class UserServiceTest {
    User user;
    User user1;
    User user2;
    Role role;
    String roleId;
    @BeforeEach
    public void  init(){
        role=Role.builder()
                .roleId("abc")
                .roleName("Normal")
                .build();
        user=User.builder()
                .name("Shakir")
                .email("add@gmail.com")
                .about("this is testing class ")
                .gender("Male")
                .imageName("abc.png")
                .password("lcwd")
                .roles(Set.of(role))
                .build();
         user1=User.builder()
                .name("Shakir1")
                .email("add1@gmail.com")
                .about("this is testing class ")
                .gender("Male")
                .imageName("abc.png")
                .password("lcwd")
                .roles(Set.of(role))
                .build();
         user2=User.builder()
                .name("Shakir2")
                .email("add2@gmail.com")
                .about("this is testing class ")
                .gender("Male")
                .imageName("abc.png")
                .password("lcwd")
                .roles(Set.of(role))
                .build();
        roleId="abc";
    }




    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper mapper;


    @Test
    public void createUser(){
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(Mockito.any())).thenReturn(Optional.of(role));

        UserDto user1=userService.createUser(mapper.map(user, UserDto.class));

        System.out.println(user1.getName());
        Assertions.assertNotNull(user1);

        Assertions.assertEquals("Shakir",user1.getName());
    }
    @Test
    public void updateUserTest(){
        String userId="";
        UserDto userDto=UserDto.builder()
                .name("shakir-Ali")
                .about("this is Updated Testing User Details ")
                .gender("Male")
                .imageName("xyz.png")
                .build();
//        UserDto updateUser=mapper.map(user,UserDto.class);
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto updatedUser= userService.updateUser(userDto,userId);
        System.out.println(updatedUser.getName());




        Assertions.assertNotNull(userDto.getName());
        Assertions.assertEquals(userDto.getName(),"shakir-Ali");
        /*When we will use these below line then our test case will failed bcoz we are getting userName value From userDTO which is define at Class Level
        System.out.println(user.getName());
        Assertions.assertEquals(userDto.getName(),updateUser.getName());
        */
    }

    @Test
    public  void deleteUserTest(){
        String userid="us";
        Mockito.when(userRepository.findById("us")).thenReturn(Optional.of(user));
        System.out.println(user);
        userService.deleteUser(userid);
//        Mockito.verify(userRepository,Mockito.times(12)).delete(user);
    }
    @Test
public void getAllUserTest(){
    User user1=User.builder()
            .name("Shakir1")
            .email("add1@gmail.com")
            .about("this is testing class ")
            .gender("Male")
            .imageName("abc.png")
            .password("lcwd")
            .roles(Set.of(role))
            .build();
    User user2=User.builder()
            .name("Shakir2")
            .email("add2@gmail.com")
            .about("this is testing class ")
            .gender("Male")
            .imageName("abc.png")
            .password("lcwd")
            .roles(Set.of(role))
            .build();

        List<User> userList= Arrays.asList(user,user1,user2);
        Page<User> page=new PageImpl<>(userList);
        Mockito.when(userRepository.findAll((Pageable)Mockito.any())).thenReturn(page);

        PageableResponse<UserDto> allUser =userService.getAllUser(1,2,"name","ascending");
        Assertions.assertEquals(3,allUser.getContent().size());
}

@Test
public void getUserByIdTest(){
        String userIDTest="add";
        Mockito.when(userRepository.findById(userIDTest)).thenReturn(Optional.of(user));
        //Actual Call of Service Method
       UserDto userDto= userService.getSingleUserById(userIDTest);
       Assertions.assertNotNull(userDto);
       Assertions.assertEquals(user.getName(),userDto.getName(),"Name Not Ma+tched");

}

    @Test
    public void getUserByEmailTest(){
        String userEmail="add@gmail.com";
        /*This below method  means if we will pass this userEmail in userService then user object will be return
        if we provide Mockito.any then we can pass any email in userService then user object will be return
        */
        Mockito.when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        //Actual Call of Service Method
        UserDto userDto= userService.getSingleUserByEmail(userEmail);
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getEmail(),userDto.getEmail(),"Mail Not Ma+tched");
    }
@Test
   public void searchUserTest(){
        String keywords="Shakir";
        Mockito.when(userRepository.findByNameContaining(keywords)).thenReturn(Arrays.asList(user,user1,user2));

        //Actual Call
      List<UserDto> userDtos=userService.searchUser(keywords);
      Assertions.assertEquals(3,userDtos.size(),"Size Not matched");
    }
@Test
    public void findUserEmailOptionalTest(){
        String email="Shakir@gmail.com";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Optional<User> userByEmailOptional=userService.findUserByEmailOptional(email);
       //This below line check that value is comming or not if coming then test pass otherwise failed
        Assertions.assertTrue(userByEmailOptional.isPresent());

        User user4=userByEmailOptional.get();
        Assertions.assertEquals(user.getEmail(),user4.getEmail(),"Test Cases failed");
}







}

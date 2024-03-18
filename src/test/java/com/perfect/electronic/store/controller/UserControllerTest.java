package com.perfect.electronic.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfect.electronic.store.dtos.PageableResponse;
import com.perfect.electronic.store.dtos.UserDto;
import com.perfect.electronic.store.entities.Role;
import com.perfect.electronic.store.entities.User;
import com.perfect.electronic.store.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Set;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;
    Role role;
    User user;
    User user1;
    User user2;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;
    @BeforeEach
    public void  init(){
        role= Role.builder()
                .roleId("abc")
                .roleName("Normal")
                .build();
        user= User.builder()
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

    }
    //We can test with another way in which... we can test controller method Directly instead of call api
    @Test
    public void createUserTest() throws  Exception{
        //users + POST + use data as json
        //data as a jsonStatus created
        UserDto dto=modelMapper.map(user, UserDto.class);
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);



        //actual reqeust for url
        this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                //This method will convert our object data into json like we pass using postman
                            .content(convertObjectTojsonString(user))
                            .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                /*in below line we will check status is created if created then test case will pass if we writ isOk() Then our test
                case will failed bcoz we are getting status Created at  Post method of UserController Class
                */
//                        .andExpect(status().isOk());

                        .andExpect(status().isCreated());
    }
    private String convertObjectTojsonString(Object user){
        try{
            return new ObjectMapper().writeValueAsString(user);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void updateTest() throws Exception{
        // user + put Request
        String userId="121";
        UserDto dto=this.modelMapper.map(user,UserDto.class);

        Mockito.when(userService.updateUser(dto,userId)).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/users/"+userId)
                        .contentType(MediaType.APPLICATION_JSON)
                //we will add authorization token becoz this is our protected api
                                .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJaYWlkSGFzYW5AZ21haWwuY29tIiwiaWF0IjoxNzA2NjIwMjg0LCJleHAiOjE3MDY2MzgyODR9.eSiaQ21YG0-j_mm4h-opiA4MVY1sOC4eldXopE_rYq3S_-n6nPFMvffZJ2BZrKvKJdPUtxzGeXch3IC4afXI0Q")
                        //This method will convert our object data into json like we pass using postman
                        .content(convertObjectTojsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getAllUsersTest() throws Exception {
       UserDto object1= UserDto.builder().name("shakir").email("Shakir@gmail.com").password("ss").about("testing").build();
       UserDto object2= UserDto.builder().name("shakir1").email("Shakir@gmail.com").password("ss").about("testing").build();
       UserDto object3= UserDto.builder().name("shakir2").email("Shakir@gmail.com").password("ss").about("testing").build();
       UserDto object4= UserDto.builder().name("shakir3").email("Shakir@gmail.com").password("ss").about("testing").build();
       UserDto object5= UserDto.builder().name("shakir4").email("Shakir@gmail.com").password("ss").about("testing").build();
       UserDto object6= UserDto.builder().name("shakir5").email("Shakir@gmail.com").password("ss").about("testing").build();
        PageableResponse<UserDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(
           object1,object2,object6,object3,object4,object5
        ));
        pageableResponse.setLastPage(false);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalElements(1000);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
                .andExpect(status().isOk());




    }
}

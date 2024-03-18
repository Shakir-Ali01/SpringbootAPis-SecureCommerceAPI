package com.perfect.electronic.store.services.impl;
import com.perfect.electronic.store.controllers.UserController;
import com.perfect.electronic.store.dtos.PageableResponse;
import com.perfect.electronic.store.dtos.UserDto;
import com.perfect.electronic.store.entities.Role;
import com.perfect.electronic.store.entities.User;
import com.perfect.electronic.store.exceptions.ResourceNotFoundException;
import com.perfect.electronic.store.helpers.Helper;
import com.perfect.electronic.store.repositories.RoleRepository;
import com.perfect.electronic.store.repositories.UserRepository;
import com.perfect.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${user.profile.image.path}")
    private String imagePath;
   @Autowired
   private PasswordEncoder passwordEncoder;
    private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Value("${normal.role.id}")
    private String normalRoleId;

    @Autowired
    private RoleRepository roleRepository;
    @Override
    public UserDto createUser(UserDto userDto) throws ResourceNotFoundException{
        Optional<User> optional= userRepository.findByEmail(userDto.getEmail());
//        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not Found with given Id"));
        if(optional.isPresent()){
            UserDto u=new UserDto();
            logger.info("if Enter Your Email Here {}",optional.get().getEmail());
            return u;
        }

            //generate Unique Id in String Format
            String userId=UUID.randomUUID().toString();
            userDto.setUserId(userId);
            //encoding password
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            //Convert Dto->entity
            User user=dtoToEntity(userDto);
            //fetch role of normal and set it to user
            Role role=roleRepository.findById(normalRoleId).get();
            user.getRoles().add(role);
            User saveUser=userRepository.save(user);
            //convert entity->dto
            UserDto newUserDto=entityToDto(saveUser);

            return userDto;


    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not Found with given Id"));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());

        //save data
        User updatedUser=userRepository.save(user);

        //convert entity to DTO
        UserDto updatedDto=entityToDto(updatedUser);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
         User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not find with given Id"));

        //delete User Profile Image
        String fullPath = imagePath + user.getImageName();
        logger.info("User Image Full Path {}",fullPath);
        try{
            Path path= Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            logger.info("User Image NOt Found In Image folder");
            ex.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        //delete user
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getSingleUserById(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("This Id user is Not Present in Database"));
        //convert entity to DTO
        UserDto userDto=entityToDto(user);
        return userDto;
    }

    @Override
    public UserDto getSingleUserByEmail(String emailId) {
        User user=userRepository.findByEmail(emailId).orElseThrow(()->new ResourceNotFoundException("This EmailId is Not Present"));
//        //convert entity to DTO
       UserDto userDto=entityToDto(user);
        return userDto;
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
//        Sort sort= Sort.by(sortBy);
        Sort sort= (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());

        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page=userRepository.findAll(pageable);
        PageableResponse<UserDto> response=Helper.getPageableResponse(page,UserDto.class);
        return response;
    }
    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users=userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList= users.stream().map(user->entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }
    @Override
    public Optional<User> findUserByEmailOptional(String email) {
        Optional<User> optional= userRepository.findByEmail(email);
        return optional;
    }

    //DTO TO Entity
    private UserDto entityToDto(User saveUser){
//       UserDto userDto= UserDto.builder()
//                .userId(saveUser.getUserId())
//                .name(saveUser.getName())
//                .email(saveUser.getEmail())
//                .about(saveUser.getAbout())
//                .gender(saveUser.getGender())
//                .password(saveUser.getPassword())
//                .imageName(saveUser.getImageName()).build();
        return mapper.map(saveUser,UserDto.class);
    }

    //Entity To DTO
    private User dtoToEntity(UserDto userDto){
//        User user=User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .password(userDto.getPassword())
//                .imageName(userDto.getImageName()).build();
        return mapper.map(userDto,User.class);
    }
}

package com.perfect.electronic.store.controllers;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.perfect.electronic.store.dtos.JwtRequest;
import com.perfect.electronic.store.dtos.JwtResponse;
import com.perfect.electronic.store.dtos.UserDto;
import com.perfect.electronic.store.entities.User;
import com.perfect.electronic.store.exceptions.BadApiRequestException;
import com.perfect.electronic.store.security.JwtHelper;
import com.perfect.electronic.store.services.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@Tag(name = "Auth Controller ", description = "APIs for Authentications")
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager; // this authenticationManager authenticate the password is correct or not

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;
    private Logger logger=LoggerFactory.getLogger(AuthController.class);

    @Value("${newPassword}")
    private String newPassword;
    @Value("${googleClientId}")
    private String googleClientId;
    @GetMapping("/t")
    public String testing(){
        return "Welcome to electronic store";
    }
    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal){
        String name=principal.getName();
        return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(name), UserDto.class), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){

        System.out.println("called Auth Login Api");
        //authenticate user name and password if it is wrong then  throw exception
        this.doAuthenticate(request.getEmail(),request.getPassword());

        //getting user Details
       UserDetails userDetails= userDetailsService.loadUserByUsername(request.getEmail());
       //Now Generate Token

       String token= this.jwtHelper.generateToken(userDetails);
       UserDto userDto=modelMapper.map(userDetails,UserDto.class);
       JwtResponse response=JwtResponse.builder().jwtToken(token).
               user(userDto).
               build();
       System.out.println("Hello ");
       return new ResponseEntity<>(response,HttpStatus.OK);
    }
    private void doAuthenticate(String email,String password){
        UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(email,password);
        try{
            authenticationManager.authenticate(authentication);
        }catch (BadCredentialsException e){
             throw new BadApiRequestException("Invalid UserName Or Password !!");
        }
    }

//    Login With Google APi

    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String,Object> data) throws IOException {
        //Get ID Token From Request
        System.out.println("Angular Calling");
        String idToken=data.get("idToken").toString();
        //Calling google to verify idToken
        NetHttpTransport netHttpTransport=new NetHttpTransport();
        JacksonFactory jacksonFactory=JacksonFactory.getDefaultInstance();
        //google id token verifier
//        Google Client ID Which is Created by google.developer.com
//        String googleClineId="";
         GoogleIdTokenVerifier.Builder verifier=new GoogleIdTokenVerifier.Builder(netHttpTransport,jacksonFactory).setAudience(Collections.singleton(googleClientId));

        //Getting Email From GoogleToken After verifying Successfully
        GoogleIdToken googleIdToken= GoogleIdToken.parse(verifier.getJsonFactory(), idToken);
        GoogleIdToken.Payload payload=googleIdToken.getPayload();
          logger.info("Payload {}",payload);
          String email=payload.getEmail();
           logger.info("Email : {}",email);
//          Verifying this email already register or Not
             User user=null;
             user=userService.findUserByEmailOptional(email).orElse(null);
//        logger.info("Already Register {}",user.getEmail());
        logger.info("Already Register {}",userService.findUserByEmailOptional(email).orElse(null));
             if(user==null){
                 //create a new User
                user= this.saveUser(email,data.get("name").toString(),data.get("photoUrl").toString());
             }
             logger.info("last Line");
             ResponseEntity<JwtResponse> jwtResponseResponseEntity=this.login(JwtRequest.builder().email(user.getEmail()).password(newPassword).build());
             return jwtResponseResponseEntity;
//        return null;
    }

    private User saveUser(String email, String name, String photoUrl) {
       UserDto newUser= UserDto.builder()
                .name(name)
                .email(email)
                .password(newPassword)
                .imageName(photoUrl)
                .roles(new HashSet<>())
                .build();
      UserDto user= userService.createUser(newUser);
      //convert userDto into userEntity
      return modelMapper.map(user,User.class);
    }
}

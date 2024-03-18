package com.perfect.electronic.store.dtos;
import com.perfect.electronic.store.Validate.ImageNameValid;
import com.perfect.electronic.store.entities.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private  String userId;
    @Size(min=3,max=15,message = "Invalid Name")
    private String name;
    //@Email(message = "Invalid User Email")
    @Pattern(regexp="^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*$",message = "Invalid User Email")
    @NotBlank(message = "Email Is Required !!")
    private String email;
    @NotBlank(message = "Password Is Required")
    private String password;
    @Size(min=4,max=6,message = "Invalid Gender")
    private String gender;
    @NotBlank(message="Write something About YourSelf")
    private String about;
    //Custom validator
//    @ImageNameValid
    private String imageName;
    private Set<RoleDto> roles=new HashSet<>();
}

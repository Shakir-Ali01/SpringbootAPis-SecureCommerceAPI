package com.perfect.electronic.store.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class JwtRequest
{
    private String  email;
    private String password;
}

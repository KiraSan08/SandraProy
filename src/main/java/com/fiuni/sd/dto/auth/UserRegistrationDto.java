package com.fiuni.sd.dto.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRegistrationDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> roles;
}
package com.example.phegon.phegonBank.auth_user.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginResponseDto {

    private String token;
    private List<String> roles;
}

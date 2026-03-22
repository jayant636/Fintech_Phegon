package com.example.phegon.phegonBank.auth_user.service;

import com.example.phegon.phegonBank.auth_user.dtos.LoginRequestDto;
import com.example.phegon.phegonBank.auth_user.dtos.LoginResponseDto;
import com.example.phegon.phegonBank.auth_user.dtos.RegistrationRequestDto;
import com.example.phegon.phegonBank.auth_user.dtos.ResetPasswordRequest;
import com.example.phegon.phegonBank.res.Response;

public interface AuthService {
    Response<String> register (RegistrationRequestDto registrationRequestDto);
    Response<LoginResponseDto> login (LoginRequestDto loginRequestDto);
    Response<?> forgotPassword(String email);
    Response<?> updatePasswordViaResetEmail(ResetPasswordRequest resetPasswordRequest);

}

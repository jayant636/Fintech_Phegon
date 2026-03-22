package com.example.phegon.phegonBank.auth_user.controller;

import com.example.phegon.phegonBank.auth_user.dtos.LoginRequestDto;
import com.example.phegon.phegonBank.auth_user.dtos.LoginResponseDto;
import com.example.phegon.phegonBank.auth_user.dtos.RegistrationRequestDto;
import com.example.phegon.phegonBank.auth_user.dtos.ResetPasswordRequest;
import com.example.phegon.phegonBank.auth_user.service.AuthService;
import com.example.phegon.phegonBank.res.Response;
import com.example.phegon.phegonBank.role.entity.RoleEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response<String>> register(@RequestBody  RegistrationRequestDto registrationRequestDto){
        return ResponseEntity.ok(authService.register(registrationRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Response<?>> forgotpassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest){
        return ResponseEntity.ok(authService.forgotPassword(resetPasswordRequest.getEmail()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Response<?>> resetpassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest){
        return ResponseEntity.ok(authService.updatePasswordViaResetEmail(resetPasswordRequest));
    }

}

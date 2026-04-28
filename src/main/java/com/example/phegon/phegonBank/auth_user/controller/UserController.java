package com.example.phegon.phegonBank.auth_user.controller;

import com.example.phegon.phegonBank.auth_user.dtos.RegistrationRequestDto;
import com.example.phegon.phegonBank.auth_user.dtos.UpdatePasswordRequest;
import com.example.phegon.phegonBank.auth_user.dtos.UserDto;
import com.example.phegon.phegonBank.auth_user.service.UserService;
import com.example.phegon.phegonBank.res.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Response<Page<UserDto>>> getAllUsers(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "50") int size){
        return ResponseEntity.ok(userService.getAllUsers(page,size));
    }

    @GetMapping("/me")
    public ResponseEntity<Response<UserDto>> getMyProfile(){
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @PutMapping("/update-password")
    public ResponseEntity<Response<?>> updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest){
        return ResponseEntity.ok(userService.updatePassword(updatePasswordRequest));
    }

    @PutMapping("/profile-picture")
    public ResponseEntity<Response<?>> updateProfilepicture(@RequestParam("file")MultipartFile file){
        return ResponseEntity.ok(userService.uploadProfilePicture(file));
    }


}

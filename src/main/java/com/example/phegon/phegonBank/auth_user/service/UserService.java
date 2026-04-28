package com.example.phegon.phegonBank.auth_user.service;

import com.example.phegon.phegonBank.auth_user.dtos.UpdatePasswordRequest;
import com.example.phegon.phegonBank.auth_user.dtos.UserDto;
import com.example.phegon.phegonBank.auth_user.entity.User;
import com.example.phegon.phegonBank.res.Response;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User getCurrentLoggedInUser();
    Response<UserDto> getMyProfile();
    Response<Page<UserDto>> getAllUsers(int page,int size);
    Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest);
    Response<?> uploadProfilePicture(MultipartFile file);

}

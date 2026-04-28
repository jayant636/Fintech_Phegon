package com.example.phegon.phegonBank.auth_user.service;


import com.example.phegon.phegonBank.auth_user.dtos.UpdatePasswordRequest;
import com.example.phegon.phegonBank.auth_user.dtos.UserDto;
import com.example.phegon.phegonBank.auth_user.entity.User;
import com.example.phegon.phegonBank.auth_user.repository.UserRepository;
import com.example.phegon.phegonBank.exceptions.NotFoundException;
import com.example.phegon.phegonBank.notification.dtos.NotificationDto;
import com.example.phegon.phegonBank.notification.service.NotificationService;
import com.example.phegon.phegonBank.res.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final String uploadDir = "uploads/profile-pictures/";

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            throw new NotFoundException("User is not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User Not Found "));
    }

    @Override
    public Response<UserDto> getMyProfile() {
        User user = getCurrentLoggedInUser();
        UserDto userDto = modelMapper.map(user, UserDto.class);

        return Response.<UserDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User retrieved")
                .data(userDto)
                .build();
    }

    @Override
    public Response<Page<UserDto>> getAllUsers(int page, int size) {
        Page<User> users = userRepository.findAll(PageRequest.of(page,size));
        Page<UserDto> userDtos = users.map(user -> modelMapper.map(user, UserDto.class));

        return Response.<Page<UserDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User retrieved")
                .data(userDtos)
                .build();
    }

    @Override
    public Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = getCurrentLoggedInUser();

        String newPassword = updatePasswordRequest.getNewPassword();
        String oldPassword = updatePasswordRequest.getOldPassword();

        if(oldPassword == null || newPassword==null){
            throw new RuntimeException("Old Or New password is not correct");
        }

        if(!passwordEncoder.matches(oldPassword,user.getPassword())){
            throw new RuntimeException("Old password doesn't match:");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        // SEND password change confirmation email
        Map<String,Object> vars = new HashMap<>();
        vars.put("name",user.getFirstName());

        NotificationDto notificationDto = NotificationDto.builder()
                .recipient(user.getEmail())
                .subject("Your password was successfully changed")
                .templateName("password-change")
                .templateVariable(vars)
                .build();

        notificationService.sendEmail(notificationDto,user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password Changed Successfully")
                .build();

    }

    @Override
    public Response<?> uploadProfilePicture(MultipartFile file) {
        User user = getCurrentLoggedInUser();

        try{
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            if(user.getProfilePicture()!= null && !user.getProfilePicture().isEmpty()){
                Path oldFile = Paths.get(user.getProfilePicture());
                if(Files.exists(oldFile)){
                    Files.delete(oldFile);
                }
            }

            //Generate a unique file name to avoid conflicts
            String originalName = file.getOriginalFilename();
            String fileExtension = "";

            if(originalName != null && originalName.contains(".")){
                fileExtension = originalName.substring(originalName.lastIndexOf("."));
            }

            String newFileName = UUID.randomUUID()+fileExtension;
            Path filePath = uploadPath.resolve(newFileName);

            Files.copy(file.getInputStream(),filePath);

            String fileUrl = uploadDir + newFileName;
            user.setProfilePicture(fileUrl);

            userRepository.save(user);

            return Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Profile picture uploaded successfully")
                    .data(fileUrl)
                    .build();

        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}

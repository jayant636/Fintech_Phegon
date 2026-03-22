package com.example.phegon.phegonBank.auth_user.service.impl;

import com.example.phegon.phegonBank.account.entity.AccountEntity;
import com.example.phegon.phegonBank.auth_user.dtos.LoginRequestDto;
import com.example.phegon.phegonBank.auth_user.dtos.LoginResponseDto;
import com.example.phegon.phegonBank.auth_user.dtos.RegistrationRequestDto;
import com.example.phegon.phegonBank.auth_user.dtos.ResetPasswordRequest;
import com.example.phegon.phegonBank.auth_user.entity.PasswordResetCode;
import com.example.phegon.phegonBank.auth_user.entity.User;
import com.example.phegon.phegonBank.auth_user.repository.PasswordResetRepository;
import com.example.phegon.phegonBank.auth_user.repository.UserRepository;
import com.example.phegon.phegonBank.auth_user.service.AuthService;
import com.example.phegon.phegonBank.auth_user.service.CodeGenerator;
import com.example.phegon.phegonBank.enums.AccountType;
import com.example.phegon.phegonBank.enums.Currency;
import com.example.phegon.phegonBank.exceptions.BadRequestException;
import com.example.phegon.phegonBank.exceptions.NotFoundException;
import com.example.phegon.phegonBank.notification.dtos.NotificationDto;
import com.example.phegon.phegonBank.notification.service.NotificationService;
import com.example.phegon.phegonBank.res.Response;
import com.example.phegon.phegonBank.role.entity.RoleEntity;
import com.example.phegon.phegonBank.role.repository.RoleRepository;
import com.example.phegon.phegonBank.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final NotificationService notificationService;

    private final CodeGenerator codeGenerator;
    private final PasswordResetRepository passwordResetRepository;

    @Value("${password.reset.link}")
    private String resetLink;


    @Override
    public Response<String> register(RegistrationRequestDto requestDto) {

        List<RoleEntity> roles;

        if(requestDto.getRoles() == null || requestDto.getRoles().isEmpty()){
            //Default to customer
            RoleEntity defaultRole = roleRepository.findByName("CUSTOMER").orElseThrow(()-> new NotFoundException("Customer Role Not Found"));
            roles = Collections.singletonList(defaultRole);
        }else{
            roles = requestDto.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new NotFoundException("ROLE NOT FOUND"+roleName)))
                    .toList();

        }
        if(userRepository.findByEmail((requestDto.getEmail())).isPresent()){
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .email(requestDto.getEmail())
                .phoneNumber(requestDto.getPhoneNumber())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .roles(roles)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        //TODO Auto generate an acount number for the user

//        AccountEntity savedaccount = accountService.createAccount(AccountType.SAVINGS,savedUser);

        //TODO Send a welcome email of the user
        Map<String, Object> vars = new HashMap<>();
        vars.put("name",savedUser.getFirstName());

        NotificationDto notificationDto = NotificationDto.builder()
                .recipient(savedUser.getEmail())
                .subject("Welcome to Phegon Bank !!!")
                .templateName("welcome")
                .templateVariable(vars)
                .build();

        notificationService.sendEmail(notificationDto,savedUser);


        //TODO Send Account details to the user email
        Map<String, Object> accountVars = new HashMap<>();
        accountVars.put("name",savedUser.getFirstName());
//        accountVars.put("accountNumber",savedaccount.getAccountNumber());
        accountVars.put("accountType",AccountType.SAVINGS.name());
        accountVars.put("currency", Currency.USD);

        NotificationDto accountcreatedEmail = NotificationDto.builder()
                .recipient(savedUser.getEmail())
                .subject("Your New Bank Account has been created")
                .templateName("account-created")
                .templateVariable(vars)
                .build();

        notificationService.sendEmail(accountcreatedEmail,savedUser);

        return Response.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Your account has been created successfully")
//                .data("Email of your account details has been sent to you. Your account number is :"+ savedaccount.getAccountNumber())
                .build();

    }

    @Override
    public Response<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
       String email = loginRequestDto.getEmail();
       String password = loginRequestDto.getPassword();

       User user = userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("Email Not found"));
       if(!passwordEncoder.matches(password, user.getPassword())){
           throw new BadRequestException("Password doesn't match");
       }
       String token = tokenService.generateToken(user.getEmail());

       LoginResponseDto loginResponseDto = LoginResponseDto.builder()
               .roles(user.getRoles().stream().map(RoleEntity::getName).toList())
               .build();

       return Response.<LoginResponseDto>builder()
               .statusCode(HttpStatus.OK.value())
               .message("Login Successful")
               .data(loginResponseDto)
               .build();
    }

    @Override
    @Transactional
    public Response<?> forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User Not found"));
        passwordResetRepository.deleteByUserId(user.getId());

        String code = codeGenerator.generateUniqueCode();
        PasswordResetCode passwordResetCode = PasswordResetCode.builder()
                .user(user)
                .code(code)
                .expiryDate(calculateExpiryDate())
                .used(false)
                .build();

        passwordResetRepository.save(passwordResetCode);

        //Send email reset link out
        Map<String,Object> templateVariable = new HashMap<>();
        templateVariable.put("name",user.getFirstName());
        templateVariable.put("resetLink",resetLink + code);

        NotificationDto notificationDto = NotificationDto
                .builder()
                .recipient(user.getEmail())
                .subject("Password Reset Code")
                .templateName("password-reset")
                .templateVariable(templateVariable)
                .build();

        notificationService.sendEmail(notificationDto,user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password reset code sent to your email")
                .build();

    }

    @Override
    @Transactional
    public Response<?> updatePasswordViaResetEmail(ResetPasswordRequest resetPasswordRequest) {
        String code = resetPasswordRequest.getCode();
        String newPassword = resetPasswordRequest.getNewPassword();

        //find & validate code
        PasswordResetCode resetCode = passwordResetRepository.findByCode(code).orElseThrow(()-> new BadRequestException("Invalid reset code"));

        //check expiration first
        if(resetCode.getExpiryDate().isBefore(LocalDateTime.now())){
            passwordResetRepository.delete(resetCode);
            throw new BadRequestException("Reset code has expired");
        }

        //update the user
        User user = resetCode.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        //delete the code immediately after the use
        passwordResetRepository.delete(resetCode);

        //Send confirmation emil
        Map<String,Object> templateVariable = new HashMap<>();
        templateVariable.put("name",user.getFirstName());

        NotificationDto confirmationEmail = NotificationDto
                .builder()
                .recipient(user.getEmail())
                .subject("Password updated successfully")
                .templateName("password-update-successfully")
                .templateVariable(templateVariable)
                .build();

        notificationService.sendEmail(confirmationEmail,user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password updated successfully")
                .build();

    }

    private LocalDateTime calculateExpiryDate(){
        return LocalDateTime.now().plusHours(5);
    }
}

package com.example.phegon.phegonBank.auth_user.dtos;

import com.example.phegon.phegonBank.account.dtos.AccountDto;
import com.example.phegon.phegonBank.role.entity.RoleEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
//Ignore any extra fields in JSON that are not present in your Java class
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {


    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    @JsonIgnore
    private String password;
    private String profilePicture;
    private boolean active = true;
    private List<RoleEntity> roles;

//    To prevent infinite recursion when serializing bidirectional JPA relationships.
//It marks the forward (parent) side of the relationship. Jackson will serialize this side normally.
    @JsonManagedReference
    private List<AccountDto> accounts;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

}

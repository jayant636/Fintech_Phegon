package com.example.phegon.phegonBank.auth_user.entity;

import com.example.phegon.phegonBank.account.entity.AccountEntity;
import com.example.phegon.phegonBank.role.entity.RoleEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    private String phoneNumber;

    @Email
    @Column(unique = true,nullable = false)
    @NotBlank(message = "Email is required")
    private String email;

    private String password;
    private String profilePicture;
    private boolean active = true;

//    A user can have many roles
//    A Role is assigned to many Users
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleEntity> roles;

//    This means User is NOT the owner of the relationship.
//    One User can have many accounts
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<AccountEntity> accounts;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

}

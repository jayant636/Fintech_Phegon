package com.example.phegon.phegonBank.account.entity;

import com.example.phegon.phegonBank.auth_user.entity.User;
import com.example.phegon.phegonBank.enums.AccountStatus;
import com.example.phegon.phegonBank.enums.AccountType;
import com.example.phegon.phegonBank.enums.Currency;
import com.example.phegon.phegonBank.transaction.entity.TransactionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 10)
    private String accountNumber;

    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<TransactionEntity> transactions = new ArrayList<>();

    private LocalDateTime closedAt;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}

package com.example.phegon.phegonBank.transaction.entity;

import com.example.phegon.phegonBank.account.entity.AccountEntity;
import com.example.phegon.phegonBank.enums.TransactionStatus;
import com.example.phegon.phegonBank.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private TransactionType transactionType;

    @Column(nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id",nullable = false)
    private AccountEntity account;

    //for transfer
    private String sourceAccount;
    private String destinationAccount;


}

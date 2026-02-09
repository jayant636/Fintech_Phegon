package com.example.phegon.phegonBank.transaction.dtos;

import com.example.phegon.phegonBank.account.dtos.AccountDto;
import com.example.phegon.phegonBank.account.entity.AccountEntity;
import com.example.phegon.phegonBank.enums.TransactionStatus;
import com.example.phegon.phegonBank.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {

    private Long id;

    private BigDecimal amount;

    private TransactionType transactionType;

    private LocalDateTime transactionDate;

    private String description;

    private TransactionStatus status;

    @JsonBackReference
    private AccountDto account;

    //for transfer
    private String sourceAccount;
    private String destinationAccount;
}

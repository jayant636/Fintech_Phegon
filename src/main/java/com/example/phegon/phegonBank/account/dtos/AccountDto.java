package com.example.phegon.phegonBank.account.dtos;

import com.example.phegon.phegonBank.auth_user.dtos.UserDto;
import com.example.phegon.phegonBank.auth_user.entity.User;
import com.example.phegon.phegonBank.enums.AccountStatus;
import com.example.phegon.phegonBank.enums.AccountType;
import com.example.phegon.phegonBank.enums.Currency;
import com.example.phegon.phegonBank.transaction.dtos.TransactionDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

    private Long id;

    private String accountNumber;

    private BigDecimal balance = BigDecimal.ZERO;

    private AccountType accountType;

    @JsonBackReference //This will not be added to the account dto . It'll be ignored coz it's a back reference
    private UserDto user;

    private Currency currency;

    private AccountStatus accountStatus;

    @JsonManagedReference //It helps avoid recursion loop by ignoring the accountDto within the transactionDto
    private List<TransactionDto> transactions;

    private LocalDateTime closedAt;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

}

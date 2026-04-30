package com.example.phegon.phegonBank.audit_dashboard.service;

import com.example.phegon.phegonBank.account.dtos.AccountDto;
import com.example.phegon.phegonBank.auth_user.dtos.UserDto;
import com.example.phegon.phegonBank.transaction.dtos.TransactionDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AuditorService {

    Map<String,Long> getSystemTotals();
    Optional<UserDto> findUserByEmail(String email);
    Optional<AccountDto> findAccountDetailsByAccountNumber(String accountNumber);
    List<TransactionDto> findTransactionByAccountNumber(String accountNumber);
    Optional<TransactionDto> findTransactionById(Long transactionId);
}

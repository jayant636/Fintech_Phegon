package com.example.phegon.phegonBank.audit_dashboard.service;

import com.example.phegon.phegonBank.account.dtos.AccountDto;
import com.example.phegon.phegonBank.account.repository.AccountRepository;
import com.example.phegon.phegonBank.auth_user.dtos.UserDto;
import com.example.phegon.phegonBank.auth_user.repository.UserRepository;
import com.example.phegon.phegonBank.transaction.dtos.TransactionDto;
import com.example.phegon.phegonBank.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class AuditorServiceImpl implements AuditorService{

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Override
    public Map<String, Long> getSystemTotals() {
        long totalUsers = userRepository.count();
        long totalAccounts = accountRepository.count();
        long totalTransactions = transactionRepository.count();


        return Map.of(
                "totalUsers" ,totalUsers,
                "totalAccounts",totalAccounts,
                "totalTransaction",totalTransactions
        );
    }

    @Override
    public Optional<UserDto> findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(user -> modelMapper.map(user, UserDto.class));
    }

    @Override
    public Optional<AccountDto> findAccountDetailsByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(account -> modelMapper.map(account, AccountDto.class));
    }

    @Override
    public List<TransactionDto> findTransactionByAccountNumber(String accountNumber) {
        return transactionRepository.findByAccount_AccountNumber(accountNumber).stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .toList();
    }

    @Override
    public Optional<TransactionDto> findTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class));
    }
}

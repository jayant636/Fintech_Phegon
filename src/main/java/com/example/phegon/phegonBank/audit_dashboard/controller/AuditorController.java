package com.example.phegon.phegonBank.audit_dashboard.controller;

import com.example.phegon.phegonBank.account.dtos.AccountDto;
import com.example.phegon.phegonBank.audit_dashboard.service.AuditorService;
import com.example.phegon.phegonBank.auth_user.dtos.UserDto;
import com.example.phegon.phegonBank.transaction.dtos.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auditor")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUDITOR')" )
public class AuditorController {

    private final AuditorService auditorService;

    @GetMapping("/totals")
    public ResponseEntity<Map<String,Long>> getSystemTotals(){
        return ResponseEntity.ok(auditorService.getSystemTotals());
    }

    @GetMapping("/users")
    public ResponseEntity<UserDto> findUserByEmail(@RequestParam String email){

        Optional<UserDto> userDto = auditorService.findUserByEmail(email);
        return userDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @GetMapping("/accounts")
    public ResponseEntity<AccountDto> findAccountsDetailsByAccountNumber(@RequestParam String accountNumber){

        Optional<AccountDto> accountDto = auditorService.findAccountDetailsByAccountNumber(accountNumber);
        return accountDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @GetMapping("/transactions/by-account")
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccountNumber(@RequestParam String accountNumber){

        List<TransactionDto> transactionDto = auditorService.findTransactionByAccountNumber(accountNumber);
        if(transactionDto.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(transactionDto);
    }

    @GetMapping("/transactions/by-id")
    public ResponseEntity<TransactionDto> getTransactionById(@RequestParam Long id){

        Optional<TransactionDto> transactionDto = auditorService.findTransactionById(id);
        return transactionDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

}

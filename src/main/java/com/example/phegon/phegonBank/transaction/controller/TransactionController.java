package com.example.phegon.phegonBank.transaction.controller;

import com.example.phegon.phegonBank.res.Response;
import com.example.phegon.phegonBank.transaction.dtos.TransactionRequest;
import com.example.phegon.phegonBank.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Response<?>> createTrnsaction(@RequestBody @Valid TransactionRequest transactionRequest){
        return ResponseEntity.ok(transactionService.createTransaction(transactionRequest));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Response<?>> createTrnsaction(@PathVariable String accountNumber , @RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "0") int size){
        return ResponseEntity.ok(transactionService.getTransactionsForAnAccount(accountNumber, page, size));
    }


}

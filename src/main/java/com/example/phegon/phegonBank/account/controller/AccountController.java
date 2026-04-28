package com.example.phegon.phegonBank.account.controller;

import com.example.phegon.phegonBank.account.service.AccountService;
import com.example.phegon.phegonBank.res.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<Response<?>> getMyAccounts(){
        return ResponseEntity.ok(accountService.getMyAccount());
    }

    @DeleteMapping("/close/{accountNumber}")
    public ResponseEntity<Response<?>> closeAccounts(@PathVariable String accountNumber){
        return ResponseEntity.ok(accountService.closeAccounts(accountNumber));
    }

}

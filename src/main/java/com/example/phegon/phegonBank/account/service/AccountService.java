package com.example.phegon.phegonBank.account.service;

import com.example.phegon.phegonBank.account.dtos.AccountDto;
import com.example.phegon.phegonBank.account.entity.AccountEntity;
import com.example.phegon.phegonBank.auth_user.entity.User;
import com.example.phegon.phegonBank.enums.AccountType;
import com.example.phegon.phegonBank.res.Response;

import java.util.List;

public interface AccountService {

    AccountEntity createAccount (AccountType accountType, User user);
    Response<List<AccountDto>> getMyAccount();
    Response<?> closeAccounts(String accountNumber);


}

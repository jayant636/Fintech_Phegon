package com.example.phegon.phegonBank.account.service;

import com.example.phegon.phegonBank.account.dtos.AccountDto;
import com.example.phegon.phegonBank.account.entity.AccountEntity;
import com.example.phegon.phegonBank.account.repository.AccountRepository;
import com.example.phegon.phegonBank.auth_user.entity.User;
import com.example.phegon.phegonBank.auth_user.service.UserService;
import com.example.phegon.phegonBank.enums.AccountStatus;
import com.example.phegon.phegonBank.enums.AccountType;
import com.example.phegon.phegonBank.enums.Currency;
import com.example.phegon.phegonBank.res.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService{

   private final AccountRepository accountRepository;
   private final UserService userService;
   private final ModelMapper modelMapper;

   private final Random random = new Random();

    @Override
    public AccountEntity createAccount(AccountType accountType, User user) {

        log.info("Inside created account");

        String accountNumber = generateAccountNumber();
        AccountEntity account = AccountEntity.builder()
                .accountNumber(accountNumber)
                .accountType(accountType)
                .currency(Currency.USD)
                .balance(BigDecimal.ZERO)
                .accountStatus(AccountStatus.ACTIVE)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        return accountRepository.save(account);
    }

    @Override
    public Response<List<AccountDto>> getMyAccount() {
        User user = userService.getCurrentLoggedInUser();

        List<AccountDto> accountDtos = accountRepository.findByUserId(user.getId())
                .stream()
                .map(account -> modelMapper.map(account, AccountDto.class))
                .toList();

        return Response.<List<AccountDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User accounts fetched successfully")
                .data(accountDtos)
                .build();
    }

    @Override
    public Response<?> closeAccounts(String accountNumber) {
        User user = userService.getCurrentLoggedInUser();

        AccountEntity account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account Number Not Found"));

        if(!user.getAccounts().contains(account)){
            throw  new RuntimeException("Account doesn't belong to you");
        }

        if(account.getBalance().compareTo(BigDecimal.ZERO) > 0){
            throw new RuntimeException("Acount balance must be zero before closing");
        }

        account.setAccountStatus(AccountStatus.CLOSED);
        account.setClosedAt(LocalDateTime.now());
        accountRepository.save(account);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account closed successfully")
                .build();
    }

    private String generateAccountNumber(){
        String accountNumber;
        do{
            //Generate a random digit number from 10000000 to 99,99,999
            //& combine it with prefix "66"
            accountNumber = "66" + (random.nextInt(90000000)+10000000);
        }while(accountRepository.findByAccountNumber(accountNumber).isPresent());
        log.info("Account Number Generated");
        return accountNumber;
    }
}

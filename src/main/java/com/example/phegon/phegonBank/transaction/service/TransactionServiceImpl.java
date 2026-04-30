package com.example.phegon.phegonBank.transaction.service;

import com.example.phegon.phegonBank.account.entity.AccountEntity;
import com.example.phegon.phegonBank.account.repository.AccountRepository;
import com.example.phegon.phegonBank.auth_user.entity.User;
import com.example.phegon.phegonBank.auth_user.service.UserService;
import com.example.phegon.phegonBank.enums.TransactionStatus;
import com.example.phegon.phegonBank.enums.TransactionType;
import com.example.phegon.phegonBank.notification.dtos.NotificationDto;
import com.example.phegon.phegonBank.notification.service.NotificationService;
import com.example.phegon.phegonBank.res.Response;
import com.example.phegon.phegonBank.transaction.dtos.TransactionDto;
import com.example.phegon.phegonBank.transaction.dtos.TransactionRequest;
import com.example.phegon.phegonBank.transaction.entity.TransactionEntity;
import com.example.phegon.phegonBank.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public Response<?> createTransaction(TransactionRequest transactionRequest) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransactionType(transactionRequest.getTransactionType());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());

        //On the basis of transaction type we'll call the transaction
        switch (transactionRequest.getTransactionType()){
            case DEPOSIT -> handleDeposit(transactionRequest,transaction);
            case WITHDRAWAL -> handleWithdrawal(transactionRequest,transaction);
            case TRANSFER -> handleTransfer(transactionRequest,transaction);
            default -> throw new RuntimeException("Invalid Transaction Type");
        }

        transaction.setStatus(TransactionStatus.SUCCESS);
        TransactionEntity savedTransacxtion = transactionRepository.save(transaction);

        //Send Notification Out
        sendTransactionNotification(savedTransacxtion);

        return Response.builder()
                .statusCode(200)
                .message("Transaction Successful")
                .build();

    }


    @Override
    public Response<List<TransactionDto>> getTransactionsForAnAccount(String accountNumber, int page, int size) {
        User user = userService.getCurrentLoggedInUser();

        AccountEntity account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account Number doesn't exist "));

//        make sure account belongs to particular user
        if(!account.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Account does not belong to authenticated user");
        }

        Pageable pageable = PageRequest.of(page,size, Sort.by("transactionDate").descending());
        Page<TransactionEntity> txns = transactionRepository.findByAccount_AccountNumber(accountNumber,pageable);

        List<TransactionDto> transactionDtos = txns.getContent().stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .toList();

        return Response.<List<TransactionDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Transaction retreived")
                .data(transactionDtos)
                .meta(Map.of(
                        "currentPage",txns.getNumber(),
                        "totalItems",txns.getTotalElements(),
                        "totalPages",txns.getTotalPages(),
                        "pageSize",txns.getSize()
                ))
                .build();

    }

    private void handleDeposit(TransactionRequest request , TransactionEntity transaction){
        AccountEntity account = accountRepository.findByAccountNumber(request.getAccountNumber()).orElseThrow(() -> new RuntimeException("Account Number doesn't exist "));

        account.setBalance(account.getBalance().add(request.getAmount()));
        transaction.setAccount(account);
        accountRepository.save(account);

    }

    private void handleWithdrawal(TransactionRequest transactionRequest , TransactionEntity transaction){
        AccountEntity account = accountRepository.findByAccountNumber(transactionRequest.getAccountNumber()).orElseThrow(() -> new RuntimeException("Account Number doesn't exist "));

        if(account.getBalance().compareTo(transactionRequest.getAmount()) < 0 ){
           throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(transactionRequest.getAmount()));
        transaction.setAccount(account);
        accountRepository.save(account);


    }

    private void handleTransfer(TransactionRequest transactionRequest , TransactionEntity transaction){
        AccountEntity sourceAccount = accountRepository.findByAccountNumber(transactionRequest.getAccountNumber()).orElseThrow(() -> new RuntimeException("Source Account Number doesn't exist "));
        AccountEntity destinationAccount = accountRepository.findByAccountNumber(transactionRequest.getAccountNumber()).orElseThrow(() -> new RuntimeException("Destination Account Number doesn't exist "));

        if(sourceAccount.getBalance().compareTo(transactionRequest.getAmount()) < 0){
            throw new RuntimeException("Insufficient balance in source account");
        }

        //deduct from source
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transactionRequest.getAmount()));
        accountRepository.save(sourceAccount);

        //Add to destination
        destinationAccount.setBalance(destinationAccount.getBalance().add(transactionRequest.getAmount()));
        accountRepository.save(destinationAccount);

        transaction.setAccount(sourceAccount);
        transaction.setSourceAccount(sourceAccount.getAccountNumber());
        transaction.setDestinationAccount(destinationAccount.getAccountNumber());
    }

    private void sendTransactionNotification(TransactionEntity txn){
        User user = txn.getAccount().getUser();
        String subject;
        String template;

        Map<String,Object> templateVariables = new HashMap<>();
        templateVariables.put("name",user.getFirstName());
        templateVariables.put("amount",txn.getAmount());
        templateVariables.put("accountNumber",txn.getAccount().getAccountNumber());
        templateVariables.put("data",txn.getTransactionDate());
        templateVariables.put("balance",txn.getAccount().getBalance());

        if(txn.getTransactionType() == TransactionType.DEPOSIT){
            subject = "Credit Alert";
            template = "credit-alert";

            NotificationDto notificationemailToSendOut = NotificationDto.builder()
                            .recipient(user.getEmail())
                                    .subject(subject)
                                            .templateName(template)
                                                    .templateVariable(templateVariables)
                                                            .build();

            notificationService.sendEmail(notificationemailToSendOut,user);
        }else if(txn.getTransactionType() == TransactionType.WITHDRAWAL){
            subject = "Debit Alert";
            template = "debit-alert";

            NotificationDto notificationEmailtosendout = NotificationDto.builder()
                    .recipient(user.getEmail())
                    .subject(subject)
                    .templateName(template)
                    .templateVariable(templateVariables)
                    .build();

            notificationService.sendEmail(notificationEmailtosendout,user);

        }else if(txn.getTransactionType() == TransactionType.TRANSFER){
            subject = "Debit Alert";
            template = "debit-alert";

            NotificationDto notificationEmailtosendout = NotificationDto.builder()
                    .recipient(user.getEmail())
                    .subject(subject)
                    .templateName(template)
                    .templateVariable(templateVariables)
                    .build();

            notificationService.sendEmail(notificationEmailtosendout,user);

            //Receiver Credit alert
            AccountEntity destination = accountRepository.findByAccountNumber(txn.getDestinationAccount()).orElseThrow(() -> new RuntimeException("Destination account not found"));

            User receiver = destination.getUser();

            Map<String,Object> recvVars = new HashMap<>();
            recvVars.put("name",receiver.getFirstName());
            recvVars.put("amount",txn.getAmount());
            recvVars.put("accountNumber",destination.getAccountNumber());
            recvVars.put("date",txn.getTransactionDate());
            recvVars.put("balance",destination.getBalance());

            NotificationDto notificationEmailsendoutToReceiver = NotificationDto.builder()
                    .recipient(receiver.getEmail())
                    .subject("Credit Alert")
                    .templateName("credit-alert")
                    .templateVariable(recvVars)
                    .build();

            notificationService.sendEmail(notificationEmailsendoutToReceiver,user);
        }



    }
}

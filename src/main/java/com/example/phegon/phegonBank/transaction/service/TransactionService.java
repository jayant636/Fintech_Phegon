package com.example.phegon.phegonBank.transaction.service;

import com.example.phegon.phegonBank.res.Response;
import com.example.phegon.phegonBank.transaction.dtos.TransactionDto;
import com.example.phegon.phegonBank.transaction.dtos.TransactionRequest;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

public interface TransactionService {

    Response<?> createTransaction(TransactionRequest transactionRequest);
    Response<List<TransactionDto>> getTransactionsForAnAccount(String accountNumber , int page, int size);


}

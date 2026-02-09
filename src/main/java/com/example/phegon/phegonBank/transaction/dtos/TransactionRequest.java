package com.example.phegon.phegonBank.transaction.dtos;

import com.example.phegon.phegonBank.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {

    private TransactionType transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String description;

    //Receiving account number if it's a transfer
    private String destinationAccountNumber;

}

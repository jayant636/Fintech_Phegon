package com.example.phegon.phegonBank.exceptions;

public class InsuffiecientBalanceException extends RuntimeException{
    public InsuffiecientBalanceException(String message) {
        super(message);
    }
}

package com.ashish.monopoly.service.exception;

public class NegativeTransactionAmountException extends RuntimeException{
    public NegativeTransactionAmountException(String message) {
        super(message);
    }
}

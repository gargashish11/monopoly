package com.ashish.monopoly.service.exception;

public class SameAccountTransaction extends RuntimeException{
    public SameAccountTransaction(String message) {
        super(message);
    }
}

package com.mybank.transactions.exceptions;

public class TransactionNotFoundException extends  RuntimeException{

    public TransactionNotFoundException(String message) {
        super(message);
    }
}

package com.citibank.transactions.exceptions;

public class InvalidRequestException extends  RuntimeException{

    public InvalidRequestException(String message) {
        super(message);
    }
}

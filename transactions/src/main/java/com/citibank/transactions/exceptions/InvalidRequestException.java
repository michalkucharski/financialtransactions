package com.citibank.transactions.exceptions;

public class InvalidRequestException extends  Exception{

    public InvalidRequestException(String message) {
        super(message);
    }
}

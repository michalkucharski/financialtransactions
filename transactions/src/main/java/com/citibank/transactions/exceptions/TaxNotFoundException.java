package com.citibank.transactions.exceptions;

public class TaxNotFoundException  extends RuntimeException{

    public TaxNotFoundException(String message) {
        super(message);
    }
}

package com.mybank.transactions.enums;

import lombok.Getter;

@Getter
public enum PaymentMethods {
    DEBIT("debit card"), CREDIT("credit card"), CASH("cash");

    /**
     * El literal del tipo de la transaccion
     */
    private String paymentMethod;


    PaymentMethods(final String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return paymentMethod;
    }
}

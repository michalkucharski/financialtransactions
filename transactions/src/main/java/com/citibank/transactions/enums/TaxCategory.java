package com.citibank.transactions.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum TaxCategory {

    ONLINESRV("REDUCEDTAX"), OTHERSRV("STNDTAX"), BASICGOODS("ZEROTAX"), OTHERGOODS("STNDTAX");

    /**
     * El literal del tipo de la transaccion
     */
    private String categoryTax;


    TaxCategory(final String categoryTax) {
        this.categoryTax = categoryTax;
    }

 }

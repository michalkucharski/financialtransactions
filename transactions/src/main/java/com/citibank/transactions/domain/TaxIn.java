package com.citibank.transactions.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaxIn {

    @JsonProperty("category of tax")
    private String taxCat;

    @JsonProperty("tax value")
    private double taxValue;
}

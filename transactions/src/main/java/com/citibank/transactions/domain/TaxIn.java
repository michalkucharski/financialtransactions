package com.citibank.transactions.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TaxIn {

    @JsonProperty("tax")
    @Getter
    @Setter
    private TaxesData taxesData;
}

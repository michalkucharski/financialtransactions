package com.citibank.transactions.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class APIResponse {

    @JsonIgnore
    @Getter
    @Setter
    private HttpStatus code;

 /*   @JsonProperty("code")
    @Getter
    @Setter
    private String internalCode;*/

    @JsonProperty("message")
    @Getter
    @Setter
    private Object body;
}

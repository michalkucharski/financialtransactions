package com.mybank.transactions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mybank.transactions.domain.TaxIn;
import com.mybank.transactions.domain.TaxesData;
import com.mybank.transactions.service.TaxServiceImpl;
import com.mybank.transactions.service.TransactionsServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    TaxServiceImpl taxService;

    @MockitoBean
    TransactionsServiceImpl transactionsServiceImpl;

    @Test
    void should_create_tax() throws Exception {

        TaxIn taxIn = new TaxIn();
        taxIn.setTaxCat("NEWTAX");
        taxIn.setTaxValue(0.7);

        doNothing().when(taxService).submitNewTax(taxIn);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions/v1/submitTransaction")
                        .contentType(MediaType.APPLICATION_JSON))
                      .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content(). string("The new transaction was submitted successfully"));

    }
}
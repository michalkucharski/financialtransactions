package com.mybank.transactions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.transactions.domain.TaxIn;
import com.mybank.transactions.domain.TaxesData;
import com.mybank.transactions.exceptions.GlobalExceptionHandler;
import com.mybank.transactions.service.TaxService;
import com.mybank.transactions.service.TaxServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TaxController.class)
public class TaxControllerTest {
    private MockMvc mockMvc;

    private TaxesData validOutput;
    @Mock
    private TaxServiceImpl taxService;

    @InjectMocks
    private TaxController taxController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // Configure exception handler for the controller
        mockMvc = MockMvcBuilders
                .standaloneSetup(taxController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Assuming you have a global exception handler
                .build();

        LocalDateTime dataRecord = LocalDateTime.now();
        validOutput = new TaxesData();
        validOutput.setTaxCat("SPECIALTAX");
        validOutput.setTaxValue(0.51);
        validOutput.setInsertDate(dataRecord.toString());
        validOutput.setUpdateDate(dataRecord.toString());
    }

    @Test
    void submitTax_ValidInput_ReturnsAccepted() throws Exception {
        // Arrange
        TaxIn validTaxIn = new TaxIn();
        validTaxIn.setTaxCat("VAT");
        validTaxIn.setTaxValue(0.21);

        LocalDateTime dataRecord = LocalDateTime.now();
        TaxesData validOutput = new TaxesData();
        validOutput.setTaxCat("VAT");
        validOutput.setTaxValue(0.21);
        validOutput.setInsertDate(dataRecord.toString());
        validOutput.setUpdateDate(dataRecord.toString());

        when(taxService.submitNewTax(any(TaxIn.class))).thenReturn(any(TaxesData.class));

        // Act & Assert
        mockMvc.perform(post("/taxes/v1/submitTax")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTaxIn)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("The new tax was submitted successfully"));

        verify(taxService, times(1)).submitNewTax(any(TaxIn.class));
    }

 /*   @Test
    void submitTax_NullInput_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/taxes/v1/submitTax")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());

        verify(taxService, never()).submitNewTax(any());
    }*/

    @Test
    void submitTax_EmptyTaxCategory_ReturnsBadRequest() throws Exception {
        // Arrange
        TaxIn taxInWithEmptyCat = new TaxIn();
        taxInWithEmptyCat.setTaxCat("");
        taxInWithEmptyCat.setTaxValue(0.21);

        // Act & Assert
        mockMvc.perform(post("/taxes/v1/submitTax")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taxInWithEmptyCat)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Tax category is not informed"));

        verify(taxService, never()).submitNewTax(any());
    }

    @Test
    void submitTax_TaxValueTooLow_ReturnsBadRequest() throws Exception {
        // Arrange
        TaxIn taxInWithLowValue = new TaxIn();
        taxInWithLowValue.setTaxCat("VAT");
        taxInWithLowValue.setTaxValue(0.05);

        // Act & Assert
        mockMvc.perform(post("/taxes/v1/submitTax")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taxInWithLowValue)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The minimal tax rate is 0.1"));

        verify(taxService, never()).submitNewTax(any());
    }

 /*   @Test
    void submitTax_ServiceThrowsException_ReturnsInternalServerError() throws Exception {
        // Arrange
        TaxIn validTaxIn = new TaxIn();
        validTaxIn.setTaxCat("VAT");
        validTaxIn.setTaxValue(0.21);

        doThrow(new RuntimeException("Database error")).when(taxService).submitNewTax(any(TaxIn.class));

        // Act & Assert
        mockMvc.perform(post("/taxes/v1/submitTax")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTaxIn)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));

        verify(taxService, times(1)).submitNewTax(any(TaxIn.class));
    }*/

    @Test
    void retrieveTax_ValidId_ReturnsTaxData() throws Exception {
        // Arrange
        given(taxService.findTaxById("SPECIALTAX")).willReturn(validOutput);
    //    when(taxService.findTaxById("SPECIALTAX")).thenReturn(validOutput);

        // Act & Assert
        mockMvc.perform(get("/taxes/v1/retrieveTax/{id}", "SPECIALTAX")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taxCat").value("SPECIALTAX"))
                .andExpect(jsonPath("$.taxValue").value(0.51));

        verify(taxService, times(1)).findTaxById("SPECIALTAX");
    }

    @Test
    void retrieveTax_EmptyId_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/taxes/v1/retrieveTax/{id}", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("TaxId is not informed "));

        verify(taxService, never()).findTaxById(anyString());
    }

    @Test
    void retrieveTax_NonExistingId_ReturnsNotFound() throws Exception {
        // Arrange
        when(taxService.findTaxById("STNDTAX")).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/taxes/v1/retrieveTax/{id}", "STNDTAX")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tax ist not founded"));

        verify(taxService, times(1)).findTaxById("STNDTAX");
    }



}

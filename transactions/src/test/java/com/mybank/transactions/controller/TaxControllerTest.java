package com.mybank.transactions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.transactions.domain.TaxIn;
import com.mybank.transactions.domain.TaxesData;
import com.mybank.transactions.service.TaxServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TaxController.class)
public class TaxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaxServiceImpl taxService;

    @Autowired
    private ObjectMapper objectMapper;


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
        TaxesData sampleTaxesData = new TaxesData();
        sampleTaxesData.setTaxCat("VAT");
        sampleTaxesData.setTaxValue(0.21);
        sampleTaxesData.setInsertDate("2025-04-18T10:15:30");
        sampleTaxesData.setUpdateDate("2025-04-18T10:15:30");

        when(taxService.findTaxById("VAT")).thenReturn(sampleTaxesData);

        // Act & Assert
        mockMvc.perform(get("/taxes/v1/retrieveTax/{id}", "VAT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taxCat").value("VAT"))
                .andExpect(jsonPath("$.taxValue").value(0.21));

        verify(taxService, times(1)).findTaxById("VAT");
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

    @Test
    void retrieveAllTaxes_ReturnsTaxData() throws Exception {
        // Arrange
        TaxesData sampleTaxesData = new TaxesData();
        sampleTaxesData.setTaxCat("VAT");
        sampleTaxesData.setTaxValue(0.21);
        sampleTaxesData.setInsertDate("2025-04-18T10:15:30");
        sampleTaxesData.setUpdateDate("2025-04-18T10:15:30");

        TaxesData sampleTaxesData1 = new TaxesData();
        sampleTaxesData1.setTaxCat("SPECIALTAX");
        sampleTaxesData1.setTaxValue(0.26);
        sampleTaxesData1.setInsertDate("2025-05-18T10:15:30");
        sampleTaxesData1.setUpdateDate("2025-05-18T10:15:30");

        List<TaxesData> taxesDataList = Arrays.asList(sampleTaxesData, sampleTaxesData1);

        when(taxService.findAllTaxes()).thenReturn(taxesDataList);

        // Act & Assert
        mockMvc.perform(get("/taxes/v1/retrieveTax")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2));

        verify(taxService, times(2)).findAllTaxes();
    }

}

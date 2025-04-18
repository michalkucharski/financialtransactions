package com.mybank.transactions.service;

import com.mybank.transactions.domain.TaxIn;
import com.mybank.transactions.domain.TaxesData;
import com.mybank.transactions.repository.TaxRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaxServiceImplTest {

    @Mock
    private TaxRepository taxRepository;

    @InjectMocks
    private TaxServiceImpl taxServiceImpl;

    private TaxesData taxesData;
    private TaxesData taxesData2;

    private TaxIn taxIn;

    @BeforeEach
    public void setup(){

        LocalDateTime auditDate = LocalDateTime.now();
        taxesData = new TaxesData("NEWTAX", 0.7, auditDate.toString(), auditDate.toString());
        taxesData2 = new TaxesData("NEWTAX2", 0.7, auditDate.toString(), auditDate.toString());

        taxIn = new TaxIn("NEWTAX", 0.7);
    }

/*    @Test
    void submitNewTax_Success() {
        // Arrange
        when(taxRepository.save(any(TaxesData.class))).thenReturn(taxesData);

        // Act
        TaxesData result = taxServiceImpl.submitNewTax(taxIn);

        // Assert
        assertNotNull(result);
        assertEquals("VAT", result.getTaxCat());
        assertEquals(0.21, result.getTaxValue());
 //       verify(taxRepository, times(1)).save(any(TaxesData.class));
    }*/

    @Test
    void findTaxById_Exists() {
        // Arrange
        when(taxRepository.findByTaxCat("VAT")).thenReturn(Stream.of(taxesData));

        // Act
        TaxesData result = taxServiceImpl.findTaxById("VAT");

        // Assert
        assertNotNull(result);
        assertEquals("VAT", result.getTaxCat());
        assertEquals(0.21, result.getTaxValue());
        verify(taxRepository, times(1)).findByTaxCat("VAT");
    }


    @Test
    void findTaxById_NotExists() {
        // Arrange
        when(taxRepository.findByTaxCat("NONEXISTENT")).thenReturn(Stream.empty());

        // Act
        TaxesData result = taxServiceImpl.findTaxById("NONEXISTENT");

        // Assert
        assertNull(result);
        verify(taxRepository, times(1)).findByTaxCat("NONEXISTENT");
    }

}
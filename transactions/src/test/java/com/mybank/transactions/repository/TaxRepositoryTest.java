package com.mybank.transactions.repository;

import com.mybank.transactions.domain.TaxesData;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class TaxRepositoryTest {
    @Autowired
    private TaxRepository taxRepository;

    private TaxesData vat;
    private TaxesData newTax;

    @BeforeEach
    void setUp() {
        // Clear the repository before each test
        taxRepository.deleteAll();

        // Create test data
        String currentTime = LocalDateTime.now().toString();

        vat = new TaxesData();
        vat.setTaxCat("VAT");
        vat.setTaxValue(0.21);
        vat.setInsertDate(currentTime);
        vat.setUpdateDate(currentTime);

        newTax = new TaxesData();
        newTax.setTaxCat("SALES");
        newTax.setTaxValue(0.15);
        newTax.setInsertDate(currentTime);
        newTax.setUpdateDate(currentTime);

        // Save test data
        taxRepository.save(vat);
        taxRepository.save(newTax);
    }

    @Test
    void findByTaxCat_ExistingTax_ReturnsMatchingTax() {
        // Act
        Stream<TaxesData> resultStream = taxRepository.findByTaxCat("VAT");
        List<TaxesData> result = resultStream.collect(Collectors.toList());

        // Assert
        assertEquals(1, result.size());
        assertEquals("VAT", result.get(0).getTaxCat());
        assertEquals(0.21, result.get(0).getTaxValue());
    }

    @Test
    void findByTaxCat_NonExistingTax_ReturnsEmptyStream() {
        // Act
        Stream<TaxesData> resultStream = taxRepository.findByTaxCat("INCOME");
        List<TaxesData> result = resultStream.collect(Collectors.toList());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByTaxCat_CaseSensitive() {
        // Act
        Stream<TaxesData> resultStream = taxRepository.findByTaxCat("vat"); // lowercase
        List<TaxesData> result = resultStream.collect(Collectors.toList());

        // Assert
        // This test verifies whether the repository query is case-sensitive
        // If the implementation should be case-insensitive, this test should be adjusted
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_ReturnsAllTaxes() {
        // Act
        List<TaxesData> result = taxRepository.findAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(tax -> "VAT".equals(tax.getTaxCat())));
        assertTrue(result.stream().anyMatch(tax -> "SALES".equals(tax.getTaxCat())));
    }

    @Test
    void save_NewTax_PersistsTax() {
        // Arrange
        String currentTime = LocalDateTime.now().toString();
        TaxesData incomeTax = new TaxesData();
        incomeTax.setTaxCat("INCOME");
        incomeTax.setTaxValue(0.30);
        incomeTax.setInsertDate(currentTime);
        incomeTax.setUpdateDate(currentTime);

        // Act
        TaxesData savedTax = taxRepository.save(incomeTax);

        // Assert
        assertNotNull(savedTax);
        assertEquals("INCOME", savedTax.getTaxCat());
        assertEquals(0.30, savedTax.getTaxValue());

        // Verify it's in the database
        List<TaxesData> allTaxes = taxRepository.findAll();
        assertEquals(3, allTaxes.size());
    }

    @Test
    void save_ExistingTax_UpdatesTax() {
        // Arrange
        // First retrieve the existing tax
        TaxesData existingVat = taxRepository.findByTaxCat("VAT").findFirst().orElseThrow();

        // Update its value
        existingVat.setTaxValue(0.23);
        String newUpdateTime = LocalDateTime.now().toString();
        existingVat.setUpdateDate(newUpdateTime);

        // Act
        TaxesData updatedTax = taxRepository.save(existingVat);

        // Assert
        assertEquals(0.23, updatedTax.getTaxValue());

        // Verify in the repository
        TaxesData freshlyRetrieved = taxRepository.findByTaxCat("VAT").findFirst().orElseThrow();
        assertEquals(0.23, freshlyRetrieved.getTaxValue());
    }
}
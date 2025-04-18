package com.mybank.transactions.service;

import com.mybank.transactions.domain.TaxesData;
import com.mybank.transactions.domain.TransactionIn;
import com.mybank.transactions.domain.TransactionsData;
import com.mybank.transactions.exceptions.TaxNotFoundException;
import com.mybank.transactions.repository.TaxRepository;
import com.mybank.transactions.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionsServiceImplTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TaxRepository taxRepository;

    @InjectMocks
    private TransactionsServiceImpl transactionsService;

    private TransactionIn sampleTransactionIn;
    private TransactionsData sampleTransactionsData;
    private TaxesData sampleTaxesData;

    @BeforeEach
    void setUp() {
        // Setup sample data for tests
        sampleTransactionIn = new TransactionIn(
                "TX123",
                1000.0,
                "DEBIT",
                "ELECTRONICS",
                "VAT"
        );

        sampleTaxesData = new TaxesData();
        sampleTaxesData.setTaxCat("VAT");
        sampleTaxesData.setTaxValue(0.2); // 20% tax

        sampleTransactionsData = new TransactionsData();
        sampleTransactionsData.setTransactionId("TX123");
        sampleTransactionsData.setGoodsType("ELECTRONICS");
        sampleTransactionsData.setGrossAmount(1000.0);
        sampleTransactionsData.setNetAmount(800.0); // 1000 - (1000 * 0.2)
        sampleTransactionsData.setTaxCat("VAT");
        sampleTransactionsData.setInsertDate(LocalDateTime.now().toString());
        sampleTransactionsData.setUpdateDate(LocalDateTime.now().toString());
    }

    @Test
    void submitTransaction_Successful() throws TaxNotFoundException {
        // Arrange
        when(taxRepository.findByTaxCat("VAT")).thenReturn(Stream.of(sampleTaxesData));
        when(transactionRepository.save(any(TransactionsData.class))).thenReturn(sampleTransactionsData);

        // Act
        transactionsService.submitTransaction(sampleTransactionIn);

        // Assert
        verify(taxRepository, times(1)).findByTaxCat("VAT");
        verify(transactionRepository, times(1)).save(any(TransactionsData.class));
    }

    @Test
    void submitTransaction_WithTaxNotFound() {
        // Arrange
        when(taxRepository.findByTaxCat("VAT")).thenReturn(Stream.empty());

        // Act & Assert
        TaxNotFoundException exception = assertThrows(TaxNotFoundException.class, () -> {
            transactionsService.submitTransaction(sampleTransactionIn);
        });

        assertEquals("The tax value was not founded", exception.getMessage());
        verify(taxRepository, times(1)).findByTaxCat("VAT");
        verify(transactionRepository, never()).save(any(TransactionsData.class));
    }

    @Test
    void submitTransaction_VerifyCalculatedAmount() throws TaxNotFoundException {
        // Arrange
        when(taxRepository.findByTaxCat("VAT")).thenReturn(Stream.of(sampleTaxesData));

        // Use argument captor to capture the saved transaction
        ArgumentCaptor<TransactionsData> transactionCaptor = ArgumentCaptor.forClass(TransactionsData.class);

        // Act
        transactionsService.submitTransaction(sampleTransactionIn);

        // Assert
        verify(transactionRepository).save(transactionCaptor.capture());
        TransactionsData savedTransaction = transactionCaptor.getValue();

        assertEquals(sampleTransactionIn.transactionId(), savedTransaction.getTransactionId());
        assertEquals(sampleTransactionIn.goodsType(), savedTransaction.getGoodsType());
        assertEquals(sampleTransactionIn.amount(), savedTransaction.getGrossAmount());
        assertEquals(sampleTransactionIn.amount() - (sampleTransactionIn.amount() * sampleTaxesData.getTaxValue()),
                savedTransaction.getNetAmount());
        assertEquals(sampleTransactionIn.taxCat().toUpperCase(), savedTransaction.getTaxCat());
        assertNotNull(savedTransaction.getInsertDate());
        assertNotNull(savedTransaction.getUpdateDate());
    }

    @Test
    void findTransactionById_Exists() {
        // Arrange
        when(transactionRepository.findByTransactionId("TX123")).thenReturn(Stream.of(sampleTransactionsData));

        // Act
        TransactionsData result = transactionsService.findTransactionyId("TX123");

        // Assert
        assertNotNull(result);
        assertEquals("TX123", result.getTransactionId());
        verify(transactionRepository, times(1)).findByTransactionId("TX123");
    }

    @Test
    void findTransactionById_NotExists() {
        // Arrange
        when(transactionRepository.findByTransactionId("NOTFOUND")).thenReturn(Stream.empty());

        // Act
        TransactionsData result = transactionsService.findTransactionyId("NOTFOUND");

        // Assert
        assertNull(result);
        verify(transactionRepository, times(1)).findByTransactionId("NOTFOUND");
    }

    @Test
    void findAllTransactions() {
        // Arrange
        TransactionsData anotherTransaction = new TransactionsData();
        anotherTransaction.setTransactionId("TX456");

        List<TransactionsData> transactions = Arrays.asList(sampleTransactionsData, anotherTransaction);
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Act
        List<TransactionsData> result = transactionsService.findAllTransactions();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("TX123", result.get(0).getTransactionId());
        assertEquals("TX456", result.get(1).getTransactionId());
        verify(transactionRepository, times(1)).findAll();
    }
}

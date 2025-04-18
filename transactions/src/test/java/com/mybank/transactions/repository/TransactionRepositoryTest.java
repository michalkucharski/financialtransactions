package com.mybank.transactions.repository;

import com.mybank.transactions.domain.TaxesData;
import com.mybank.transactions.domain.TransactionsData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    private TransactionsData sell1;
    private TransactionsData sell2;

    @BeforeEach
    void setUp() {
        // Clear the repository before each test
        transactionRepository.deleteAll();

        // Create test data
        String currentTime = LocalDateTime.now().toString();

        sell1 = new TransactionsData();
        sell1.setTransactionId("5546");
        sell1.setNetAmount(100);
        sell1.setGrossAmount(120);
        sell1.setGoodsType("food");
        sell1.setPaymentType("DEBIT");
        sell1.setTaxCat("IVA");
        sell1.setInsertDate(currentTime);
        sell1.setUpdateDate(currentTime);

        sell2 = new TransactionsData();
        sell2.setTransactionId("5547");
        sell2.setNetAmount(1000);
        sell2.setGrossAmount(1500);
        sell2.setGoodsType("electronic device");
        sell2.setPaymentType("DEBIT");
        sell2.setTaxCat("IVA");
        sell2.setInsertDate(currentTime);
        sell2.setUpdateDate(currentTime);

        // Save test data
        transactionRepository.save(sell1);
        transactionRepository.save(sell2);
    }

    @Test
    void findByTrxId_ExistingTrx_ReturnsMatchingTrx() {
        // Act
        Stream<TransactionsData> resultTrxStream = transactionRepository.findByTransactionId("5547");
        List<TransactionsData> resultTrx = resultTrxStream.collect(Collectors.toList());

        // Assert
        assertEquals(1, resultTrx.size());
        assertEquals("5547", resultTrx.get(0).getTaxCat());
        assertEquals("electronic device", resultTrx.get(0).getGoodsType());
    }

    @Test
    void findByTrxId_NonExistingTrx_ReturnsEmptyStream() {
        // Act
        Stream<TransactionsData> resultTrxStream = transactionRepository.findByTransactionId("123332");
        List<TransactionsData> resultTrx = resultTrxStream.collect(Collectors.toList());

        // Assert
        assertTrue(resultTrx.isEmpty());
    }

    @Test
    void findByTrxId_UseAlphanumericCharacters() {
        // Act
        Stream<TransactionsData> resultTrxStream = transactionRepository.findByTransactionId("v125ftrdusjew"); // lowercase
        List<TransactionsData> resultTrx = resultTrxStream.collect(Collectors.toList());

        // Assert
        // This test defines how is the response when we use alphanumeric characters to find the result by id
        assertTrue(resultTrx.isEmpty());
    }
}
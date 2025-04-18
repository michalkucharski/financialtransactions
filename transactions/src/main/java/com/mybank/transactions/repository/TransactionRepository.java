package com.mybank.transactions.repository;

import com.mybank.transactions.domain.TransactionsData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface TransactionRepository extends MongoRepository<TransactionsData, String> {

    public Stream<TransactionsData> findByTransactionId(String transactionId);
}

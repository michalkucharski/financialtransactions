package com.citibank.transactions.repository;

import com.citibank.transactions.domain.TransactionsData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface TransactionRepository extends MongoRepository<TransactionsData, String> {

    public Stream<TransactionsData> findByTransactionId(String transactionId);
}

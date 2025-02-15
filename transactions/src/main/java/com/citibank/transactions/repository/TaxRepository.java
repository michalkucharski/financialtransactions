package com.citibank.transactions.repository;

import com.citibank.transactions.domain.TaxesData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface TaxRepository extends MongoRepository<TaxesData, String> {

    public Stream<TaxesData> findByTaxId(String taxId);
}

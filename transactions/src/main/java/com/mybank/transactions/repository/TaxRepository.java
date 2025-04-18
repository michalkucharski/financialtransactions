package com.mybank.transactions.repository;

import com.mybank.transactions.domain.TaxesData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface TaxRepository extends MongoRepository<TaxesData, String> {

    public Stream<TaxesData> findByTaxCat(String taxCat);
}

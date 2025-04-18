package com.mybank.transactions.service;

import com.mybank.transactions.domain.TaxIn;
import com.mybank.transactions.domain.TaxesData;
import com.mybank.transactions.repository.TaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaxServiceImpl  implements TaxService{

    @Autowired
    TaxRepository taxRepository;

    @Override
    public TaxesData submitNewTax(TaxIn taxIn) {
        var taxData = new TaxesData();
        LocalDateTime auditDate = LocalDateTime.now();
        taxData.setTaxCat(taxIn.getTaxCat());
        taxData.setTaxValue(taxIn.getTaxValue());
        taxData.setInsertDate(auditDate.toString());
        taxData.setUpdateDate(auditDate.toString());
        return taxRepository.save(taxData);
    }

    @Override
    public TaxesData findTaxById(String taxCat) {
        return taxRepository.findByTaxCat(taxCat).findFirst().orElse(null);
    }

    @Override
    public List<TaxesData> findAllTaxes() {
        return taxRepository.findAll();
    }
}

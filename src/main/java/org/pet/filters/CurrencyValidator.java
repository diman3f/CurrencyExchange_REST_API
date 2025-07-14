package org.pet.filters;

import org.pet.dao.CurrencyRepository;
import org.pet.dao.JDBCCurrencyRepository;

import java.util.Currency;

public class CurrencyValidator implements Validator {

    public CurrencyValidator() {
    }

    @Override
    public org.pet.entity.Currency getCurrencyByCode(String code) {
        if (isValidCurrencyCode(code)) {
            org.pet.entity.Currency currency = org.pet.entity.Currency.builder()
                    .code(code)
                    .build();
            return currency;
        }
        throw new RuntimeException("Код не является допустимым кодом валюты по ISO 4217");
    }


    protected boolean isValidCurrencyCode(String code) {
        try {
            Currency currency = Currency.getInstance(code);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Код не является допустимым кодом валюты по ISO 4217");
        }
    }


    protected static boolean isValidCurrencyFullName(String name) {
        String regex = "[a-zA-Z]{64}";
        return name.matches(regex);
    }
}

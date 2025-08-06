package org.pet.filters;

import org.pet.exception.CurrencyException;
import org.pet.exception.ValidationException;

import java.util.Currency;

public class CurrencyValidator implements Validator {

    public CurrencyValidator() {
    }

    @Override
    public org.pet.entity.Currency getCurrencyByCode(String code) {
        try {
            Currency currencyUtil = Currency.getInstance(code);
            org.pet.entity.Currency currency = org.pet.entity.Currency.builder()
                    .code(code)
                    .build();
            return currency;
        } catch (Exception e) {
            throw new ValidationException("The code is not a valid ISO 4217 currency code");
        }
    }

    public void validateCurrencyAttributes(String name, String code, String sign) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Currency name must not be empty");
        }
        if (code == null || code.isBlank()) {
            throw new ValidationException("Currency code must not be empty");
        }
        if (sign == null || sign.isBlank()) {
            throw new ValidationException("Currency sign must not be empty");
        }
        if (name.length() > 64) {
            throw new ValidationException("Currency name must not exceed 64 characters");
        }
        if (!isValidCurrencyFullName(name)) {
            throw new ValidationException("Currency name must contain only letters and spaces");
        }
        if (isValidCurrencyCode(code)) {
            Currency currencyUtil = Currency.getInstance(code);
            if (!currencyUtil.getSymbol().equals(sign))
                throw new ValidationException("The sign is not a valid ISO 4217 currency sign");
        }
    }


    protected boolean isValidCurrencyCode(String code) {
        try {
            Currency.getInstance(code);
            return true;
        } catch (Exception e) {
            throw new ValidationException("The code is not a valid ISO 4217 currency code");
        }
    }


    protected boolean isValidCurrencyFullName(String name) {
        String regex = "^[a-zA-Zа ]+$";
        return name.matches(regex);
    }

}


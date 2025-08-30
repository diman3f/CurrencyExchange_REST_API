package org.pet.filters;

import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.exception.ValidationException;

import java.math.BigDecimal;
import java.util.Currency;

public class CurrencyValidator implements Validator {

    private static final int FULL_LENGTH_NAME_CURRENCY = 64;

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

    public void validateExchangeRateRequestGetMethod(ExchangeRateRequestServletDTO dto) {

        String baseCode = dto.getBaseCode();
        String targetCode = dto.getTargetCode();
        validateCurrencyCode(baseCode);
        validateCurrencyCode(targetCode);
    }

    public void validateExchangeRateRequestDtoFromPatchInfo(ExchangeRateRequestServletDTO dto) {
        String baseCode = dto.getBaseCode();
        String targetCode = dto.getTargetCode();
        BigDecimal rate = dto.getRate();
        validateCurrencyCode(baseCode);
        validateCurrencyCode(targetCode);
        validateRateExchangeRate(rate);

    }

    public void validateCurrencyAttributes(String name, String code, String sign) {
        validateName(name);
        validateCurrencyCode(code);
        validateCurrencySign(code, sign);
    }

    protected void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Currency name must not be empty");
        }
        if (!isValidCurrencyFullName(name)) {
            throw new ValidationException("Currency name must contain only letters and spaces");
        }
        validateLengthFullNameCurrency(name);
    }

    protected void validateCurrencyCode(String code) {
        if (code == null || code.isBlank()) {
            throw new ValidationException("Currency name must not be empty");
        }
        try {
            Currency.getInstance(code);
        } catch (Exception e) {
            throw new ValidationException(String.format("The code %s is not a valid ISO 4217 currency code", code));
        }
    }

    protected void validateCurrencySign(String code, String sign) {
        if (code == null || sign.isBlank()) {
            throw new ValidationException("Currency name must not be empty");
        }
        Currency currencyUtil = Currency.getInstance(code);
        String signUtil = currencyUtil.getSymbol();
        if (!currencyUtil.getSymbol().equals(sign))
            throw new ValidationException(String.format("The sign is not a valid ISO 4217 currency, sign currency - %s", signUtil));
    }

    protected void validateLengthFullNameCurrency(String name) {
        if (name.length() > FULL_LENGTH_NAME_CURRENCY) {
            throw new ValidationException("Currency name must not exceed 64 characters");
        }
    }

    protected boolean isValidCurrencyFullName(String name) {
        String regex = "^[a-zA-Zа ]+$";
        return name.matches(regex);
    }

    protected void validateRateExchangeRate(BigDecimal rate) {
        if (rate.doubleValue() <= 0) {
            throw new ValidationException("The exchange rate must be greater than 0");
        }
    }
}


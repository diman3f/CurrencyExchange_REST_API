package org.pet.context;

import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.entity.Currency;

public interface Validator {
    Currency getCurrencyByCode(String code);
    void validateCurrencyAttributes(String name, String code, String sign);
    void validateExchangeRateRequestGetMethod(ExchangeRateRequestServletDTO dto);
    void validateExchangeRateRequestDtoFromPatchInfo(ExchangeRateRequestServletDTO dto);
}

package org.pet.filters;

import org.pet.entity.Currency;

public interface Validator {
    Currency getCurrencyByCode(String code);
}

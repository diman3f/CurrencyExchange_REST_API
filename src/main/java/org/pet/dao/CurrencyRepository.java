package org.pet.dao;

import org.pet.entity.Currency;

public interface CurrencyRepository {
   Currency findByCode(String code);
}

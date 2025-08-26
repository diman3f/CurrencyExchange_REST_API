package org.pet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.pet.entity.Currency;

import java.math.BigDecimal;
@Builder
@Getter
@Setter
public class CurrencyExchangeRateResponseDto extends BaseDto {

    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
    private double amount;
    private BigDecimal convertedAmount;



}


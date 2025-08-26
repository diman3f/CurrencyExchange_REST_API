package org.pet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CurrencyExchangeRateRequestDto {

    private String baseCurrency;
    private String targetCurrency;
    private double amount;


}

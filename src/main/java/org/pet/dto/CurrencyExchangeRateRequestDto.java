package org.pet.dto;

import lombok.Builder;

@Builder
public class CurrencyExchangeRateRequestDto {

    private String baseCurrency;
    private String targetCurrency;
    private double amount;

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

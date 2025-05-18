package org.pet.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class CurrencyExchangeRateRequestDto {

    private String baseCurrency;
    private String targetCurrency;
    private double amount;


    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

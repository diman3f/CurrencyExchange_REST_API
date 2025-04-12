package org.pet.dto;

import lombok.Builder;
import java.math.BigDecimal;
@Builder
public class ExchangeRateRequestServletDTO {
    private String baseCode;
    private String targetCode;
    private BigDecimal rate;


    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}

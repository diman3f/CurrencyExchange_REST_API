package org.pet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Builder
@Getter
@Setter
public class ExchangeRateRequestServletDTO {
    private String baseCode;
    private String targetCode;
    private BigDecimal rate;

}

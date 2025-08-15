package org.pet.context;

import jakarta.servlet.http.HttpServletRequest;
import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.mapper.ExchangeRateMapper;

import java.math.BigDecimal;
import java.net.http.HttpRequest;

public class BuilderRequestDto {

    public ExchangeRateRequestServletDTO createExchangeRateDto(HttpServletRequest req) {
        String codeBase = req.getParameter("baseCurrencyCode");
        String codeTarget = req.getParameter("targetCurrencyCode");
        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(req.getParameter("rate")));
        return ExchangeRateMapper.INSTANCE.toExchangeRateRequestDtoBuilder(codeBase, codeTarget, rate);
    }
}

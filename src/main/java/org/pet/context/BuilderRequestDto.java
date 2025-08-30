package org.pet.context;

import jakarta.servlet.http.HttpServletRequest;
import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.mapper.ExchangeRateMapper;
import org.pet.utils.ValidatorURLUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class BuilderRequestDto {

    public ExchangeRateRequestServletDTO createExchangeRateDtoFromParameter(HttpServletRequest req) {
        String baseCode = req.getParameter("baseCurrencyCode");
        String targetCode = req.getParameter("targetCurrencyCode");
        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(req.getParameter("rate")));
        return ExchangeRateMapper.INSTANCE.toExchangeRateRequestDtoBuilder(baseCode, targetCode, rate);
    }

    public ExchangeRateRequestServletDTO createExchangeRateDtoFromPathInfo(HttpServletRequest req) throws IOException {
        ExchangeRateRequestServletDTO exchangeRateDto = createExchangeRateDtoGetMethod(req);
        String body = req.getReader().readLine();
        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(body.split("=", 2)[1]));
        return ExchangeRateMapper.INSTANCE.toExchangeRateRequestDtoBuilder(exchangeRateDto.getBaseCode(), exchangeRateDto.getTargetCode(), rate);
    }

    public ExchangeRateRequestServletDTO createExchangeRateDtoGetMethod(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        List<String> codes = ValidatorURLUtil.getValidPairCurrencyPairFormat(pathInfo);
        String baseCode = codes.get(0);
        String targetCode = codes.get(1);
        return ExchangeRateMapper.INSTANCE.toExchangeRateRequestDtoBuilder(baseCode, targetCode);
    }
}

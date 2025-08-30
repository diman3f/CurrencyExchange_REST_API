package org.pet.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.entity.Currency;
import org.pet.entity.ExchangeRate;
import java.math.BigDecimal;

@Mapper
public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    @Mapping(target = "id", source = "exchangeRate.id")
    @Mapping(target = "baseCurrency", source = "base")
    @Mapping(target = "targetCurrency", source = "target")
    @Mapping(target = "rate", source = "exchangeRate.rate")
    ExchangeRateResponseDTO toExchangeRate(Currency base, Currency target, ExchangeRate exchangeRate);

    ExchangeRateRequestServletDTO toExchangeRate(String baseCode, String targetCode, BigDecimal rate);

    ExchangeRateRequestServletDTO toExchangeRate(String baseCode, String targetCode);


}

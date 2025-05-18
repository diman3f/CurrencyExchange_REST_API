package org.pet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.entity.Currency;
import org.pet.entity.ExchangeRate;

@Mapper
public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    @Mapping(target = "id", source = "exchangeRate.id")
    @Mapping(target = "baseCurrency", source = "base")
    @Mapping(target = "targetCurrency", source = "target")
    @Mapping(target = "rate", source = "exchangeRate.rate")
    ExchangeRateResponseDTO toExchangeRateResponseDTO(Currency base, Currency target, ExchangeRate exchangeRate);


    @Mapping(target = "baseCode", source = "parameter", qualifiedByName = "toBaseCodeCurrencyFromParameter")
    @Mapping(target = "targetCode", source = "parameter", qualifiedByName = "toTargetCodeCurrencyFromParameter")
    ExchangeRateRequestServletDTO toExchangeRateRequestDto(String parameter);

    @Named("toBaseCodeCurrencyFromParameter")
    default String getBaseCode(String parameter) {
        return parameter.substring(1, 4);
    }

    @Named("toTargetCodeCurrencyFromParameter")
    default String getTargetCode(String parameter) {
        return parameter.substring(4);
    }
}

package org.pet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.entity.Currency;
import org.pet.entity.ExchangeRate;

@Mapper
public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);
    @Mapping(target = "id", source ="exchangeRate.id")
    @Mapping(target = "rate", source ="exchangeRate.rate")
    @Mapping(target = "baseCurrency", source ="base")
    @Mapping(target = "targetCurrency", source ="target")
    ExchangeRateResponseDTO toExchangeRateDTO(Currency base, Currency target, ExchangeRate exchangeRate);



}

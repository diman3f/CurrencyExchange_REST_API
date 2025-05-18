package org.pet.mapper;

import jakarta.servlet.http.HttpServletRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.pet.dto.CurrencyExchangeRateRequestDto;

@Mapper
public interface CurrencyExchangeMapper {
    CurrencyExchangeMapper INSTANCE = Mappers.getMapper(CurrencyExchangeMapper.class);
    CurrencyExchangeRateRequestDto toDto(String baseCurrency, String targetCurrency, double amount);
}

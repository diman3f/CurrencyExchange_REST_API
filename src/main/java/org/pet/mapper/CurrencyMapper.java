package org.pet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.pet.dto.CurrencyDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.entity.Currency;
import org.pet.entity.ExchangeRate;

import java.math.BigDecimal;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);
    CurrencyDTO toCurrencyDTO(Currency currency);
    Currency toCurrency(CurrencyDTO currencyDTO);



}

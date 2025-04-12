package org.pet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.pet.dto.CurrencyDTO;
import org.pet.entity.Currency;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);
    CurrencyDTO toCurrencyDTO(Currency currency);
    Currency toCurrency(CurrencyDTO currencyDTO);



}

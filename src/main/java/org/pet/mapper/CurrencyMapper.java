package org.pet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.pet.dto.CurrencyDTO;
import org.pet.entity.Currency;

import java.util.List;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);
    CurrencyDTO toCurrencyDTO(Currency currency);
    List<CurrencyDTO> toCurrencyDTOList(List<Currency> currencies);
    Currency toCurrency(CurrencyDTO currencyDTO);



}

package org.pet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.pet.dto.CurrencyDTO;
import org.pet.dto.CurrencyRequestDto;
import org.pet.entity.Currency;

import java.util.List;
import java.util.Set;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    @Mapping(target = "name", source = "nameCurrency")
    @Mapping(target = "code", source = "codeCurrency")
    @Mapping(target = "sign", source = "signCurrency")
    CurrencyRequestDto toCurrencyRequestDTO(String nameCurrency, String codeCurrency, String signCurrency);
    CurrencyDTO toCurrencyDTO(Currency currency);
    List<CurrencyDTO> toCurrencyDTOList(Set<Currency> currencies);
    Currency toCurrency(CurrencyRequestDto currencyDTO);



}

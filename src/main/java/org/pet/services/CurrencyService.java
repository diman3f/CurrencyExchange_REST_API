package org.pet.services;

import org.pet.dao.CurrencyDbManager;
import org.pet.dto.CurrencyDTO;
import org.pet.dto.CurrencyServletDTO;

import java.util.ArrayList;
import java.util.List;


public class CurrencyService {

    private CurrencyDbManager dataAccessObject = new CurrencyDbManager();

    public List<CurrencyServletDTO> getCurrencies() {
        List<CurrencyServletDTO> currencyServletDTOs = new ArrayList<>();

        List<CurrencyDTO> currencyEntities = dataAccessObject.getAllEntities();
        for (CurrencyDTO currencyEntity : currencyEntities) {
            currencyServletDTOs.add(toDTO(currencyEntity));
        }
        return currencyServletDTOs;
    }

    public CurrencyServletDTO getCurrency(CurrencyDTO currencyDTO) {
        CurrencyDTO dto = dataAccessObject.getEntity(currencyDTO);
        CurrencyServletDTO currencyServletDTO = toDTO(dto);
        return currencyServletDTO;
    }

    public CurrencyServletDTO createCurrency(CurrencyDTO currencyDTO) {
        CurrencyDTO dto = dataAccessObject.createEntity(currencyDTO);
        CurrencyServletDTO currencyServletDTO = toDTO(dto);
        return currencyServletDTO;
    }









    private CurrencyServletDTO toDTO(CurrencyDTO currencyDTO) {
        CurrencyServletDTO currencyServletDTO = new CurrencyServletDTO();

        currencyServletDTO.setCode(currencyDTO.getCode());
        currencyServletDTO.setFull_name(currencyDTO.getFull_name());
        currencyServletDTO.setSign(currencyDTO.getSign());

        return currencyServletDTO;
    }
}

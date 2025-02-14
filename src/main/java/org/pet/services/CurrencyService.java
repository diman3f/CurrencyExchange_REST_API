package org.pet.services;

import org.pet.dao.CurrencyDbManager;
import org.pet.dao.DataAccessObject;
import org.pet.dto.CurrencyDTO;
import org.pet.dto.CurrencyServletDTO;


public class CurrencyService {

    private CurrencyDbManager dataAccessObject = new CurrencyDbManager();
    int counterRequestCurrency = 0;

    public CurrencyServletDTO getCurrency(int id) {
        CurrencyDTO currencyDTO = dataAccessObject.readEntity(id);
        counterRequestCurrency = +currencyDTO.getId();
       CurrencyServletDTO currencyServletDTO = toDTO(currencyDTO);

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

package main.services;

import main.dao.CurrencyStorage;
import main.dto.CurrencyDTO;
import main.model.CurrencyEntity;


public class CurrencyService {

    private CurrencyStorage storage;


    public CurrencyService(CurrencyStorage storage) {
        this.storage = storage;

    }

    public void addNewCurrency(CurrencyDTO currencyDTO) {
        var code = currencyDTO.getCode();
        var fullName = currencyDTO.getFullName();
        CurrencyEntity currency = new CurrencyEntity(0, code, fullName);
        storage.addCurrency(currency);

    }
}

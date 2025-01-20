package main.dao;

import main.model.CurrencyEntity;

import java.util.HashMap;
import java.util.Map;

public class CurrencyStorage {
    private static int id = 1;
    private Map<Integer, CurrencyEntity> currencyMap = new HashMap<>();

    public void addCurrency(CurrencyEntity currency) {
        currencyMap.put(id, currency);
        System.out.printf("Валюта добавлена, id валюты - %d,  говорит %s, %n", id, this.getClass());
        id++;
    }
}

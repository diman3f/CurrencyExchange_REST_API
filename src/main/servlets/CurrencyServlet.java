package main.servlets;

import main.CodeCurrency;

import main.dto.CurrencyDTO;
import main.services.CurrencyService;



public class CurrencyServlet {

    private final CurrencyService service;

    public CurrencyServlet(CurrencyService service) {
        this.service = service;
    }
    public void getCurrency(CodeCurrency currency) {
    }
    public void POST(String code, String fullName) {
        CurrencyDTO currencyDTO = new CurrencyDTO();
        service.addNewCurrency(currencyDTO);
    }
}

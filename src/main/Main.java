package main;

import main.dao.CurrencyStorage;
import main.services.CurrencyService;
import main.servlets.CurrencyServlet;

public class Main {
    public static void main(String[] args) {

        CurrencyStorage storage = new CurrencyStorage();
        CurrencyService service = new CurrencyService(storage);
        CurrencyServlet servlet = new CurrencyServlet(service);
        servlet.POST("USD", "US Dollar ");
        servlet.POST("RUB", "RUSSIAN FEDERATION");
        servlet.POST("EUR", "Euro");




    }
}

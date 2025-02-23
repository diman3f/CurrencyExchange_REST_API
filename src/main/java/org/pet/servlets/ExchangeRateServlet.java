package org.pet.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dao.CurrencyDbManager;
import org.pet.dto.ExchangeRateDTO;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRateServlet extends HttpServlet {
    private CurrencyDbManager currencyDbManager;

    @Override
    public void init() throws ServletException {
        this.currencyDbManager = new CurrencyDbManager();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<ExchangeRateDTO> dtoList = currencyDbManager.findAllExchangeRate();

        resp.setContentType("text/html");

        var printWriter = resp.getWriter();
        for(ExchangeRateDTO rateDTO : dtoList) {
            printWriter.println(("<td>" + rateDTO.toString() + "</td>"));
        }




    }
}

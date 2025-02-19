package org.pet.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dto.CurrencyDTO;
import org.pet.dto.CurrencyServletDTO;
import org.pet.services.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private CurrencyService currencyService;


    @Override
    public void init() {
        currencyService = new CurrencyService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        List<CurrencyServletDTO> currencies = currencyService.getCurrencies();
        for (CurrencyServletDTO currency : currencies) {
            PrintWriter printWriter = resp.getWriter();
            printWriter.println("<td>" + currency.getCode() + "</td>");
            printWriter.println("<td>" + currency.getFull_name() + "</td>");
            printWriter.println("<td>" + currency.getSign() + "</td>");
            printWriter.println("<tr>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDTO dto = new CurrencyDTO();
        Map<String, String[]> parameterMap = req.getParameterMap();
        String[] code = parameterMap.get("code");
        String[] name = parameterMap.get("name");
        String[] sign = parameterMap.get("sign");
        dto.setCode(code[0]);
        dto.setFull_name(name[0]);
        dto.setSign(sign[0]);

        currencyService.createCurrency(dto);
    }
}



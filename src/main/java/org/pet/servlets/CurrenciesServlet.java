package org.pet.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dto.CurrencyServletDTO;
import org.pet.services.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {


    public CurrenciesServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CurrencyService currencyService = new CurrencyService();
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
}



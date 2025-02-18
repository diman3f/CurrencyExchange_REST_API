package org.pet.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dto.CurrencyDTO;
import org.pet.dto.CurrencyServletDTO;
import org.pet.services.CurrencyService;

import java.io.IOException;


@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    CurrencyService currencyService;

    @Override
    public void init()  {
        currencyService = new CurrencyService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codeCurrency = req.getParameter("*");
        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setCode(codeCurrency);
        CurrencyServletDTO  currency = currencyService.getCurrency(currencyDTO);
        resp.setContentType("text/html");
        var printWriter = resp.getWriter();
        printWriter.println(("<td>" + currency.getCode() + "</td>"));
        printWriter.println(("<td>" + currency.getFull_name() + "</td>"));
        printWriter.println(("<td>" + currency.getSign() + "</td>"));
    }
}

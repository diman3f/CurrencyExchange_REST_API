package org.pet.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dao.CurrencyDbManager;
import org.pet.dto.ExchangeRateDTO;
import org.pet.dto.ExchangeRateRequestDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.services.ExchangeRateService;
import org.pet.utils.ConnectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Map;


@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private CurrencyDbManager currencyDbManager;
    private Connection connection;
    private ExchangeRateService exchangeRateService; //todo нужно будет заменить на внедрение интерфейса


    @Override
    public void init() throws ServletException {
        this.currencyDbManager = new CurrencyDbManager();
        this.connection = ConnectionManager.open();
        this.exchangeRateService = new ExchangeRateService();

    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("PATCH".equals(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String parameter = req.getParameter("*");
        ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(parameter);
        ExchangeRateResponseDTO exchangeRateDTO = exchangeRateService.getExchangeRateDTO(exchangeRateRequestDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(exchangeRateDTO);
        resp.getWriter().write(json);

    }


    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String parameter = req.getParameter("*");

        String baseCurrencyCode = parameter.substring(0, 3);
        String targetCurrencyCode = parameter.substring(3);

        String s = req.getReader().readLine();
        Double rate = Double.parseDouble(s.split("=", 2)[1]);



        int result = currencyDbManager.updateExchangeRate(baseCurrencyCode, targetCurrencyCode, rate, connection);

        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html");
        writer.print(("<td>" + result + "</td>"));
    }

}

package org.pet.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dto.CurrencyExchangeRateRequestDto;
import org.pet.dto.CurrencyExchangeRateResponseDto;
import org.pet.mapper.CurrencyExchangeMapper;
import org.pet.services.ExchangeRateService;
import org.pet.utils.ConnectionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/exchange/*")
public class ExchangeServlet extends HttpServlet {
    private ExchangeRateService service;

    @Override
    public void init() throws ServletException {
        this.service = new ExchangeRateService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrency = req.getParameter("from");
        String targetCurrency = req.getParameter("to");
        double amount = Double.parseDouble(req.getParameter("amount"));
        CurrencyExchangeRateRequestDto dto = CurrencyExchangeMapper.INSTANCE.toDto(baseCurrency, targetCurrency, amount);
        CurrencyExchangeRateResponseDto currencyExchangeRateResponseDto = service.executeExchangeCurrency(dto);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        resp.getWriter().write(mapper.writeValueAsString(currencyExchangeRateResponseDto));
    }
}

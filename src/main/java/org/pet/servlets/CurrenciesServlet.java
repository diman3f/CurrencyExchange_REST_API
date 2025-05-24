package org.pet.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dao.CurrencyDao;
import org.pet.dto.CurrencyDTO;
import org.pet.entity.Currency;
import org.pet.exception.CurrencyException;
import org.pet.utils.ConnectionManager;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDao instance = CurrencyDao.getINSTANCE();
        List<Currency> currencies = new ArrayList<>();
        currencies.addAll(instance.findAllCurrencies());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = objectMapper.writeValueAsString(currencies);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDao instance = CurrencyDao.getINSTANCE();

        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        CurrencyDTO dto = CurrencyDTO.builder()
                .id(0)
                .name(name)
                .code(code)
                .sign(sign)
                .build();
        Optional<Currency> currency = instance.createCurrency(dto);
        if (currency.isPresent()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            String json = objectMapper.writeValueAsString(currency.get());
            resp.getWriter().write(json);
        } else {
            throw new CurrencyException("Валюта добавлена ранее");
        }
    }
}



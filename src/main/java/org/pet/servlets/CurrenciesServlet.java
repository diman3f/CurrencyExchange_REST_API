package org.pet.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dao.CurrencyDao;
import org.pet.dto.CurrencyDTO;
import org.pet.entity.Currency;
import org.pet.exception.CurrencyException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    @Override
    public void init() {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDao instance = CurrencyDao.getINSTANCE();
        List<Currency> currencies = new ArrayList<>();
        currencies = instance.findAllCurrencies();
        for (Currency currency : currencies) {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(currency);
            resp.getWriter().write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDao instance = CurrencyDao.getINSTANCE();
        CurrencyDTO dto = createCurrencyDTOFromRequest(req);
        Optional<Currency> currency = instance.createCurrency(dto);
        if (currency.isPresent()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(currency.get());
            resp.getWriter().write(json);
        } else {
            throw new CurrencyException("Валюта добавлена ранее");
        }
    }
    private CurrencyDTO createCurrencyDTOFromRequest(HttpServletRequest req) { //todo Не универсальный методо, либу в утилитный либо подумать как мапить дто из реквеста
        return CurrencyDTO.builder()
                .code(req.getParameter("code"))
                .full_name(req.getParameter("full_name"))
                .sign(req.getParameter("sign"))
                .build();
    }
}



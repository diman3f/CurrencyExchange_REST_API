package org.pet.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dao.CurrencyDao;
import org.pet.dto.CurrencyDTO;
import org.pet.entity.Currency;
import org.pet.exception.CurrencyException;
import org.pet.exception.DaoException;
import org.pet.exception.URLEncodingException;
import org.pet.mapper.CurrencyMapper;
import org.pet.utils.JsonResponseBuilder;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends ExceptionHandler {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String codeCurrency = req.getParameter("*");
        if (codeCurrency.isEmpty()) {
            throw new URLEncodingException("Код валюты отсутствует в адресе");
        }
        try {
            CurrencyDao instance = CurrencyDao.getINSTANCE();
            Optional<Currency> currency = instance.findByCode(codeCurrency);
            CurrencyDTO dto = CurrencyMapper.INSTANCE.toCurrencyDTO(currency.orElseThrow());
            JsonResponseBuilder.buildJsonResponse(resp, dto);
        } catch (DaoException e) {
            throw new CurrencyException(e.getMessage());
        }
    }
}




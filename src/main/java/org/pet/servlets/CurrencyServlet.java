package org.pet.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.context.ApplicationInitializer;
import org.pet.context.ServiceLocator;
import org.pet.dao.JDBCCurrencyRepository;
import org.pet.dto.CurrencyDTO;
import org.pet.entity.Currency;
import org.pet.exception.CurrencyException;
import org.pet.exception.DaoException;
import org.pet.exception.URLEncodingException;
import org.pet.filters.CurrencyValidator;
import org.pet.filters.Validator;
import org.pet.mapper.CurrencyMapper;
import org.pet.utils.JsonResponseBuilder;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private Validator currencyValidator;

    @Override
    public void init() throws ServletException {
       this.currencyValidator = (Validator) ServiceLocator.getService(CurrencyValidator.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String code = req.getParameter("*");
        currencyValidator.getCurrencyByCode(code);

        String codeCurrency = req.getParameter("*");
        if (codeCurrency.isEmpty()) {
            throw new URLEncodingException("Код валюты отсутствует в адресе");

        }
        try {
            JDBCCurrencyRepository instance = JDBCCurrencyRepository.getINSTANCE();
            Currency currency = instance.findByCode(codeCurrency);
            CurrencyDTO dto = CurrencyMapper.INSTANCE.toCurrencyDTO(currency);
            JsonResponseBuilder.buildJsonResponse(resp, dto);
        } catch (DaoException e) {
            throw new CurrencyException(e.getMessage());
        }
    }
}




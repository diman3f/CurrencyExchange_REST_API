package org.pet.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.context.ServiceLocator;
import org.pet.dao.CurrencyDAO;
import org.pet.dto.CurrencyDTO;
import org.pet.entity.Currency;
import org.pet.exception.*;
import org.pet.filters.CurrencyValidator;
import org.pet.filters.Validator;
import org.pet.mapper.CurrencyMapper;
import org.pet.utils.ExceptionHandlerUtil;
import org.pet.utils.JsonResponseBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private Validator currencyValidator;

    @Override
    public void init() throws ServletException {
        this.currencyValidator = (Validator) ServiceLocator.getService(CurrencyValidator.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            CurrencyDAO instance = CurrencyDAO.getINSTANCE();
            List<Currency> currencies = new ArrayList<>();
            currencies.addAll(instance.findAllCurrencies());
            List<CurrencyDTO> currencyDTOList = CurrencyMapper.INSTANCE.toCurrencyDTOList(currencies);
            JsonResponseBuilder.buildJsonResponse(resp, currencyDTOList);
        } catch (RuntimeException e) {
            ExceptionHandlerUtil.handleException(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            CurrencyDAO instance = CurrencyDAO.getINSTANCE();

            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String sign = req.getParameter("sign");
            currencyValidator.validateCurrencyAttributes(name, code, sign);
            CurrencyDTO dto = CurrencyDTO.builder()
                    .id(null)
                    .name(name)
                    .code(code)
                    .sign(sign)
                    .build();
            Currency currency = instance.createCurrency(dto);
            CurrencyDTO currencyDto = CurrencyMapper.INSTANCE.toCurrencyDTO(currency);
            JsonResponseBuilder.buildJsonResponse(resp, currencyDto, 201);
        } catch (RuntimeException e) {
            ExceptionHandlerUtil.handleException(resp, e);
        }
    }
}



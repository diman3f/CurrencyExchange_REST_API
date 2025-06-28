package org.pet.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dao.CurrencyDao;
import org.pet.dto.CurrencyDTO;
import org.pet.entity.Currency;
import org.pet.exception.*;
import org.pet.mapper.CurrencyMapper;
import org.pet.utils.JsonResponseBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/currencies")
public class CurrenciesServlet extends ExceptionHandler {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            CurrencyDao instance = CurrencyDao.getINSTANCE();
            List<Currency> currencies = new ArrayList<>();
            currencies.addAll(instance.findAllCurrencies());
            List<CurrencyDTO> currencyDTOList = CurrencyMapper.INSTANCE.toCurrencyDTOList(currencies);
            JsonResponseBuilder.buildJsonResponse(resp, currencyDTOList);
        } catch (RuntimeException e) {
            JsonResponseBuilder.buildExceptionResponse(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDao instance = CurrencyDao.getINSTANCE();

        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        if (name.isEmpty() || code.length()!=3 || sign.isEmpty()) {
            throw new URLEncodingException("Отсутствует нужное поле формы");
        }
        CurrencyDTO dto = CurrencyDTO.builder()
                .id(0)
                .name(name)
                .code(code)
                .sign(sign)
                .build();

        try {
            Optional<Currency> currency = instance.createCurrency(dto);
            CurrencyDTO currencyDto = CurrencyMapper.INSTANCE.toCurrencyDTO(currency.orElseThrow());
            JsonResponseBuilder.buildJsonResponse(resp, currencyDto);
        } catch (DaoException e) {
            throw new CurrencyPairAlreadyExistsException(e.getMessage());
        }
    }
}



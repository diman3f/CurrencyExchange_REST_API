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
import org.pet.exception.CurrencyException;
import org.pet.exception.MissingRequiredValueException;
import org.pet.filters.CurrencyValidator;
import org.pet.filters.Validator;
import org.pet.mapper.CurrencyMapper;
import org.pet.utils.ExceptionHandlerUtil;
import org.pet.utils.JsonResponseBuilder;

import java.io.IOException;

@WebServlet("/currency/*")

public class CurrencyServlet extends HttpServlet {

    private Validator currencyValidator;

    @Override
    public void init() throws ServletException {
        this.currencyValidator = (Validator) ServiceLocator.getService(CurrencyValidator.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            String pathInfo = req.getPathInfo();
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                String code = pathParts[1];
                currencyValidator.getCurrencyByCode(code);
                CurrencyDAO instance = CurrencyDAO.getINSTANCE();
                Currency currency = instance.findByCode(code);
                CurrencyDTO dto = CurrencyMapper.INSTANCE.toCurrencyDTO(currency);
                JsonResponseBuilder.buildJsonResponse(resp, dto);
            } else {
                throw new MissingRequiredValueException("Currency code is missing in the URL path");
            }
        } catch (RuntimeException e) {
            ExceptionHandlerUtil.handleException(resp, e);
        }
    }
}





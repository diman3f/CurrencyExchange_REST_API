package org.pet.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.HttpStatus;
import org.pet.context.BuilderRequestDto;
import org.pet.context.ServiceLocator;
import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.exception.*;
import org.pet.context.CurrencyValidator;
import org.pet.context.Validator;
import org.pet.services.ExchangeRateService;
import org.pet.utils.ExceptionHandlerUtil;
import org.pet.utils.JsonResponseBuilder;
import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private Validator currencyValidator;
    private BuilderRequestDto builderRequestDto;

    @Override
    public void init() throws ServletException {
        this.currencyValidator = (Validator) ServiceLocator.getService(CurrencyValidator.class);
        this.builderRequestDto = (BuilderRequestDto) ServiceLocator.getService(BuilderRequestDto.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<ExchangeRateResponseDTO> dto = ExchangeRateService.getINSTANCE().getAllExchangeRate();
            JsonResponseBuilder.buildJsonResponse(resp, dto, HttpStatus.OK);
        } catch (DataBaseException e) {
            throw new ExchangeRateException(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ExchangeRateRequestServletDTO dto = builderRequestDto.createExchangeRateDtoFromParameter(req);
            currencyValidator.validateExchangeRateRequestGetMethod(dto);
            ExchangeRateResponseDTO exchangeRateResponseDTO = ExchangeRateService.getINSTANCE().saveExchangeRate(dto);
            JsonResponseBuilder.buildJsonResponse(resp, exchangeRateResponseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            ExceptionHandlerUtil.handleException(resp, e);
        }
    }
}

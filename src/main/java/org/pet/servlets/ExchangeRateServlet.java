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
import org.pet.context.CurrencyValidator;
import org.pet.context.Validator;
import org.pet.services.ExchangeRateService;
import org.pet.utils.ExceptionHandlerUtil;
import org.pet.utils.JsonResponseBuilder;
import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private Validator currencyValidator;
    private BuilderRequestDto builderRequestDto;

    @Override
    public void init() throws ServletException {
        this.currencyValidator = (Validator) ServiceLocator.getService(CurrencyValidator.class);
        this.builderRequestDto = (BuilderRequestDto) ServiceLocator.getService(BuilderRequestDto.class);
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ExchangeRateRequestServletDTO exchangeRateDto= builderRequestDto.createExchangeRateDtoGetMethod(req);
            currencyValidator.validateExchangeRateRequestGetMethod(exchangeRateDto);
            ExchangeRateResponseDTO exchangeRateDTO = ExchangeRateService.getINSTANCE().getExchangeRate(exchangeRateDto);
            JsonResponseBuilder.buildJsonResponse(resp, exchangeRateDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            ExceptionHandlerUtil.handleException(resp, e);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ExchangeRateRequestServletDTO exchangeRateDtoFromPathInfo = builderRequestDto.createExchangeRateDtoFromPathInfo(req);
            currencyValidator.validateExchangeRateRequestDtoFromPatchInfo(exchangeRateDtoFromPathInfo);
            ExchangeRateResponseDTO exchangeRateDto = ExchangeRateService.getINSTANCE().updateExchangeRate(exchangeRateDtoFromPathInfo);
            JsonResponseBuilder.buildJsonResponse(resp, exchangeRateDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            ExceptionHandlerUtil.handleException(resp, e);
        }
    }
}

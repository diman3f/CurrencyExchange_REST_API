package org.pet.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.context.ServiceLocator;
import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.exception.ExchangeRateException;
import org.pet.exception.ValidationException;
import org.pet.filters.CurrencyValidator;
import org.pet.filters.Validator;
import org.pet.mapper.ExchangeRateMapper;
import org.pet.services.ExchangeRateService;
import org.pet.utils.ExceptionHandlerUtil;
import org.pet.utils.JsonResponseBuilder;
import org.pet.utils.ValidatorURLUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private Validator currencyValidator;

    @Override
    public void init() throws ServletException {
        this.currencyValidator = (Validator) ServiceLocator.getService(CurrencyValidator.class);
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
            String pathInfo = req.getPathInfo();

            List<String> codes = ValidatorURLUtil.getValidPairCurrencyPairFormat(pathInfo);
            String baseCode = codes.get(0);
            String targetCode = codes.get(1);

            currencyValidator.getCurrencyByCode(baseCode);
            currencyValidator.getCurrencyByCode(targetCode);

            ExchangeRateRequestServletDTO dto = ExchangeRateRequestServletDTO.builder()
                    .baseCode(baseCode)
                    .targetCode(targetCode)
                    .build();
//            ExchangeRateRequestServletDTO dto = ExchangeRateMapper.INSTANCE.toExchangeRateRequestDto(сurrencyPair);
            ExchangeRateResponseDTO exchangeRateDTO = ExchangeRateService.getINSTANCE().getExchangeRate(dto);
            JsonResponseBuilder.buildJsonResponse(resp, exchangeRateDTO, 200);
        } catch (ValidationException e) {
            ExceptionHandlerUtil.handleException(resp, e);
        }
    }


    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String parameter = req.getPathInfo();
        if (parameter.length() != 7) {
            JsonResponseBuilder.buildExceptionResponse(resp, new ValidationException("Отсутствует нужное поле формы"));
        }
        try {
            ExchangeRateRequestServletDTO exchangeRateRequestServletDTO = ExchangeRateMapper.INSTANCE.toExchangeRateRequestDto(parameter);
            String s = req.getReader().readLine();
            double rate = Double.parseDouble(s.split("=", 2)[1]);
            exchangeRateRequestServletDTO.setRate(BigDecimal.valueOf(rate));
            ExchangeRateResponseDTO exchangeRateDTO = ExchangeRateService.getINSTANCE().updateExchangeRate(exchangeRateRequestServletDTO);
            JsonResponseBuilder.buildJsonResponse(resp, exchangeRateDTO, 200);
        } catch (RuntimeException e) {
            resp.setStatus(404);
            JsonResponseBuilder.buildExceptionResponse(resp, new ExchangeRateException("Валютная пара отсутствует в базе данных"));
        }
    }
}

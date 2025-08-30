package org.pet.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.exception.*;
import org.pet.services.ExchangeRateService;
import org.pet.utils.JsonResponseBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends ExceptionHandler {

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ExchangeRateRequestServletDTO dto = dtoExchangeRateRequest(req);
            ExchangeRateResponseDTO exchangeRateResponseDTO = ExchangeRateService.getINSTANCE().saveExchangeRate(dto);
            JsonResponseBuilder.buildJsonResponse(resp, exchangeRateResponseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            ExceptionHandlerUtil.handleException(resp, e);

    private ExchangeRateRequestServletDTO dtoExchangeRateRequest(HttpServletRequest req) {
        String codeBase = req.getParameter("baseCurrencyCode");
        String codeTarget = req.getParameter("targetCurrencyCode");
        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(req.getParameter("rate")));
        String rateLength = String.valueOf(rate.doubleValue());
        if (codeBase.length() == 3 && codeTarget.length() == 3 && rate.doubleValue() > 0 && rateLength.length() < 9) {
            return ExchangeRateRequestServletDTO.builder()
                    .baseCode(codeBase)
                    .targetCode(codeTarget)
                    .rate(rate)
                    .build();
        }
        throw new URLEncodingException("Отсутствует нужное поле формы");
    }
}

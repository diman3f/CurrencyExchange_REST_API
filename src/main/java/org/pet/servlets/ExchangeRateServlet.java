package org.pet.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.exception.ExchangeRateException;
import org.pet.exception.URLEncodingException;
import org.pet.mapper.ExchangeRateMapper;
import org.pet.services.ExchangeRateService;
import org.pet.utils.JsonResponseBuilder;

import java.io.IOException;
import java.math.BigDecimal;


@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends ExceptionHandler {
    
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
        String parameter = req.getParameter("*");
        if (parameter.length() != 6) {
            throw new URLEncodingException("Коды валют пары отсутствуют в адресе");
        }
        ExchangeRateRequestServletDTO dto = ExchangeRateMapper.INSTANCE.toExchangeRateRequestDto(parameter);
        ExchangeRateResponseDTO exchangeRateDTO = ExchangeRateService.getINSTANCE().getExchangeRate(dto);
        JsonResponseBuilder.buildJsonResponse(resp, exchangeRateDTO);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String parameter = req.getPathInfo();
        if (parameter.length() != 7) {
            JsonResponseBuilder.buildExceptionResponse(resp, new URLEncodingException("Отсутствует нужное поле формы"));
        }
        try {
            ExchangeRateRequestServletDTO exchangeRateRequestServletDTO = ExchangeRateMapper.INSTANCE.toExchangeRateRequestDto(parameter);
            String s = req.getReader().readLine();
            double rate = Double.parseDouble(s.split("=", 2)[1]);
            exchangeRateRequestServletDTO.setRate(BigDecimal.valueOf(rate));
            ExchangeRateResponseDTO exchangeRateDTO = ExchangeRateService.getINSTANCE().updateExchangeRate(exchangeRateRequestServletDTO);
            JsonResponseBuilder.buildJsonResponse(resp, exchangeRateDTO);
        } catch (RuntimeException e) {
            resp.setStatus(404);
            JsonResponseBuilder.buildExceptionResponse(resp, new ExchangeRateException("Валютная пара отсутствует в базе данных"));
        }
    }
}

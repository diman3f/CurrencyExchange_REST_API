package org.pet.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.exception.DaoException;
import org.pet.services.ExchangeRateService;
import org.pet.utils.ConnectionManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRateService service;

    @Override
    public void init() throws ServletException {
        this.service = new ExchangeRateService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<ExchangeRateResponseDTO> dtoList = service.getAllExchangeRate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            resp.getWriter().write(mapper.writeValueAsString(dtoList));
        } catch (DaoException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateRequestServletDTO dto = dtoExchangeRateRequest(req);
        try {
            ExchangeRateResponseDTO exchangeRateResponseDTO = service.saveExchangeRate(dto);
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            resp.getWriter().write(mapper.writeValueAsString(exchangeRateResponseDTO));
        } catch (IOException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private ExchangeRateRequestServletDTO dtoExchangeRateRequest(HttpServletRequest req) {
        String codeBase = req.getParameter("baseCurrencyCode");
        String codeTarget = req.getParameter("targetCurrencyCode");
        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(req.getParameter("rate")));
        return ExchangeRateRequestServletDTO.builder()
                .baseCode(codeBase)
                .targetCode(codeTarget)
                .rate(rate)
                .build();
    }
}

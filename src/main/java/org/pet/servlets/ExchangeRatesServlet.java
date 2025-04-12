package org.pet.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private Connection connection;
    private ExchangeRateService service;

    @Override
    public void init() throws ServletException {
        this.connection = ConnectionManager.open();
        this.service = new ExchangeRateService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<ExchangeRateResponseDTO> dtoList = service.getAllExchangeRate();
            resp.setContentType("application/json");
            ObjectMapper mapper = new ObjectMapper();
            for (ExchangeRateResponseDTO dto : dtoList) {
                resp.getWriter().write(mapper.writeValueAsString(dto));
            }
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
            resp.getWriter().write(mapper.writeValueAsString(exchangeRateResponseDTO));
        } catch (IOException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private ExchangeRateRequestServletDTO dtoExchangeRateRequest(HttpServletRequest req) {
        String codeBase = req.getParameter("baseCodeCurrency");
        String codeTarget = req.getParameter("targetCodeCurrency");
        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(req.getParameter("rate")));
        return ExchangeRateRequestServletDTO.builder()
                .baseCode(codeBase)
                .targetCode(codeTarget)
                .rate(rate)
                .build();
    }
}

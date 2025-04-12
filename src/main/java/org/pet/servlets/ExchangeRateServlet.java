package org.pet.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.mapper.ExchangeRateMapper;
import org.pet.services.ExchangeRateService;
import org.pet.utils.ConnectionManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;


@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private Connection connection;

    @Override
    public void init() throws ServletException {
        this.connection = ConnectionManager.open();
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String parameter = req.getParameter("*");
        ExchangeRateRequestServletDTO dto = ExchangeRateMapper.INSTANCE.toExchangeRateRequestDto(parameter);
        ExchangeRateResponseDTO exchangeRateDTO = ExchangeRateService.getINSTANCE().getExchangeRate(dto);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(exchangeRateDTO);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String parameter = req.getParameter("*");
        ExchangeRateRequestServletDTO exchangeRateRequestServletDTO = ExchangeRateMapper.INSTANCE.toExchangeRateRequestDto(parameter);
        String s = req.getReader().readLine();
        Double rate = Double.parseDouble(s.split("=", 2)[1]);
        exchangeRateRequestServletDTO.setRate(BigDecimal.valueOf(rate));
        ExchangeRateResponseDTO exchangeRateDTO = ExchangeRateService.getINSTANCE().updateExchangeRate(exchangeRateRequestServletDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(exchangeRateDTO);
        resp.getWriter().write(json);
    }
}

package org.pet.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dao.CurrencyDao;
import org.pet.dao.CurrencyDbManager;
import org.pet.dao.ExchangeRateDao;
import org.pet.dto.ExchangeRateDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.exception.DaoException;
import org.pet.services.ExchangeRateService;
import org.pet.utils.ConnectionManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private CurrencyDbManager currencyDbManager;
    private Connection connection;

    @Override
    public void init() throws ServletException {
        this.currencyDbManager = new CurrencyDbManager();
        this.connection = ConnectionManager.open();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateService service = new ExchangeRateService();
        try {
            List<ExchangeRateResponseDTO> dtoList = service.getAllExchangeRate();
            resp.setContentType("application/json");
            ObjectMapper mapper = new ObjectMapper();
            for(ExchangeRateResponseDTO dto : dtoList) {
                resp.getWriter().write(mapper.writeValueAsString(dto));
            }

        } catch (DaoException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String codeBase = req.getParameter("baseCodeCurrency");
        String codeTarget = req.getParameter("targetCodeCurrency");
        Double rate = Double.parseDouble(req.getParameter("rate"));

        int result = currencyDbManager.createExchangeRate(codeBase, codeTarget, rate, connection);

        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html");
        writer.print(("<td>" + result + "</td>"));
    }
}

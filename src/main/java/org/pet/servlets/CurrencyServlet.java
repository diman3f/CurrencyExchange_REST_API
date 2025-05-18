package org.pet.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dao.CurrencyDao;
import org.pet.entity.Currency;
import org.pet.utils.ConnectionManager;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    @Override
    public void init() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String codeCurrency = req.getParameter("*");
        CurrencyDao instance = CurrencyDao.getINSTANCE();
        Connection connection = ConnectionManager.getConnection();
        Optional<Currency> currency = instance.findByCode(codeCurrency);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = objectMapper.writeValueAsString(currency.get());
        resp.getWriter().write(json);
    }
}

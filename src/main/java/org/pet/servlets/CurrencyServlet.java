package org.pet.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dao.CurrencyDao;
import org.pet.entity.Currency;
import org.pet.utils.ConnectionManager;

import java.io.IOException;
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
        Optional<Currency> currency = instance.findByCode(codeCurrency, ConnectionManager.open());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(currency.get());
        resp.getWriter().write(json);
    }
}

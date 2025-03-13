package org.pet.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dao.CurrencyDao;
import org.pet.dto.CurrencyDTO;
import org.pet.dto.CurrencyServletDTO;
import org.pet.entity.Currency;
import org.pet.mapper.CurrencyMapper;
import org.pet.services.CurrencyService;

import java.io.IOException;
import java.util.Optional;


@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    CurrencyService currencyService;

    @Override
    public void init() {
        currencyService = new CurrencyService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codeCurrency = req.getParameter("*");
        CurrencyDao instance = CurrencyDao.getINSTANCE();
        Optional<CurrencyDTO> currency = instance.findByCode(codeCurrency);
        System.out.println();
        if (currency.isPresent()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(currency.get());
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(json);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString("Такой валюты нет");
            resp.sendRedirect("/currenc/*");

            resp.getWriter().write(json);
        }





    }
}

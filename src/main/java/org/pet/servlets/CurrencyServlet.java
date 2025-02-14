package org.pet.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dto.CurrencyServletDTO;
import org.pet.services.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/id")
public class CurrencyServlet extends HttpServlet {


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public CurrencyServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        CurrencyService currencyService = new CurrencyService();
        CurrencyServletDTO currency = currencyService.getCurrency(id);
        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();

        printWriter.println("<td>" + currency.getCode() + "</td>");
        printWriter.println("<td>" + currency.getFull_name() + "</td>");
        printWriter.println("<td>" + currency.getSign() + "</td>");
        printWriter.println("<td>" + "привет" + "</td>");
        printWriter.println("<tr>");
    }
}

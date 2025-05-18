package org.pet.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.exception.InvalidParameterCurrencyException;
import java.io.IOException;
import java.rmi.RemoteException;

//@WebFilter(filterName = "ValidateInputUrlParameter", urlPatterns = {"/currency/*"})
public class ValidateInputUrlParameter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String code = req.getParameter("*");
        if (code != null) {
            validateCodeCurrency(code);
            chain.doFilter(req, res);
        } else {
            throw new RemoteException("параметр null");
        }
    }

    private void validateCodeCurrency(String parameterCode) {
        boolean correctFormatCode = parameterCode.matches("^[A-Z]{3}+$") || parameterCode.matches("^[A-Z]{6}+$");
        if (!correctFormatCode) {
            throw new InvalidParameterCurrencyException("Укажите параметр code в формате ХХХ или ХХХYYY");
        }
    }
}
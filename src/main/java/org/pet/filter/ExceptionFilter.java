package org.pet.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.exception.ExceptionHandler;

import java.io.IOException;

@WebFilter(filterName = "ExceptionFilter", urlPatterns = {"/*"})
public class ExceptionFilter extends HttpFilter {

    ExceptionHandler exceptionHandler;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws
            IOException, ServletException {
        try {
            chain.doFilter(req, res);
        } catch (RuntimeException e) {
            exceptionHandler = new ExceptionHandler(e);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            String string = objectMapper.writeValueAsString("message " + e.getMessage());
            res.setStatus(exceptionHandler.getCode());
            res.getWriter().write(string);
            e.printStackTrace();
        }
    }
}


package org.pet.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ResponseFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        setContentType(res);
        super.doFilter(req, res, chain);
    }

    private static void setContentType(HttpServletResponse httpResponse) {
        httpResponse.setContentType("application/json");
        httpResponse.setCharacterEncoding("UTF-8");
    }
}

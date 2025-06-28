package org.pet.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.exception.*;
import org.pet.utils.JsonResponseBuilder;

import java.io.IOException;


public abstract class ExceptionHandler extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            super.service(req, resp);
        } catch (URLEncodingException e) {
            resp.setStatus(400);
            JsonResponseBuilder.buildExceptionResponse(resp, e);
        } catch (DataBaseException e) {
            resp.setStatus(500);
            JsonResponseBuilder.buildExceptionResponse(resp, e);
        } catch (CurrencyException | ExchangeRateException e) {
            resp.setStatus(404);
            JsonResponseBuilder.buildExceptionResponse(resp, e);
        } catch (CurrencyPairAlreadyExistsException e) {
            resp.setStatus(409);
            JsonResponseBuilder.buildExceptionResponse(resp, e);
        }
    }
}



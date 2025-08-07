package org.pet.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.context.ServiceLocator;
import org.pet.exception.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public final class ExceptionHandlerUtil {

    private static ObjectMapper mapper = (ObjectMapper) ServiceLocator.getService(ObjectMapper.class);

    private ExceptionHandlerUtil() {
        throw new ArrayStoreException("Нельзя создать объект статического класса");
    }

    public static void handleException(HttpServletResponse response, RuntimeException exception) {
        
        if (exception instanceof CurrencyNotFoundException) {
            buildExceptionResponse(response, exception,404);
        }
        if (exception instanceof CurrencyAlreadyExistsException) {
            buildExceptionResponse(response, exception,409);
        }
        if (exception instanceof ValidationException) {
            buildExceptionResponse(response, exception,400);
        }

        if (exception instanceof DataBaseException) {
            buildExceptionResponse(response, exception,500);
        }
    }


    public static void buildExceptionResponse(HttpServletResponse response, RuntimeException e, int codeException) {
        Map<String, String> errorMessage = Map.of("message", e.getMessage());
        response.setStatus(codeException);
        try (PrintWriter writer = response.getWriter();) {
            writer.write(mapper.writeValueAsString(errorMessage));
        } catch (IOException exception) {
            e.printStackTrace();
        }
    }
}



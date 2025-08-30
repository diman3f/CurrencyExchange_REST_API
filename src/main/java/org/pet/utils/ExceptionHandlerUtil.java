package org.pet.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.HttpStatus;
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
            buildExceptionResponse(response, exception, HttpStatus.NOT_FOUND);
        }
        if (exception instanceof CurrencyAlreadyExistsException) {
            buildExceptionResponse(response, exception, HttpStatus.CONFLICT);
        }
        if (exception instanceof ValidationException) {
            buildExceptionResponse(response, exception, HttpStatus.BAD_REQUEST);
        }

        if (exception instanceof DataBaseException) {
            buildExceptionResponse(response, exception, HttpStatus.INTERNAL_SERVER_ERROR); //todo Ошибку 500 нужно писать в логи она не обрабатывается
        }
    }

    public static void buildExceptionResponse(HttpServletResponse response, RuntimeException e, HttpStatus httpStatus) {
        Map<String, String> errorMessage = createMessageResponse(e.getMessage());
        response.setStatus(httpStatus.getCode());
        try (PrintWriter writer = response.getWriter();) {
            writer.write(mapper.writeValueAsString(errorMessage));
        } catch (IOException exception) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> createMessageResponse(String message) {
        return Map.of("message", message);
    }

}



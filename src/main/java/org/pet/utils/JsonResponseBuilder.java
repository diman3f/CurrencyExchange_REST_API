package org.pet.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;

public final class JsonResponseBuilder extends HttpServlet {
    private final static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private JsonResponseBuilder() {
        throw new ArrayStoreException("Нельзя создать объект статического класса");
    }

    public static void buildJsonResponse(HttpServletResponse response, Object object, HttpStatus statusCode) {
        response.setStatus(statusCode.getCode());
        try (PrintWriter writer = response.getWriter()) {
            writer.write(mapper.writeValueAsString(object));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}

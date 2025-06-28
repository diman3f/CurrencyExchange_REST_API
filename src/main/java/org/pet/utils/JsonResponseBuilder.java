package org.pet.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.pet.dto.BaseDto;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public final class JsonResponseBuilder extends HttpServlet {
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void buildExceptionResponse(HttpServletResponse response, RuntimeException e) {
        setContentType(response);
        Map<String, String> errorMessage = Map.of("message", e.getMessage());
        try (PrintWriter writer = response.getWriter();) {
            writer.write(mapper.writeValueAsString(errorMessage));
        } catch (IOException exception) {
            e.printStackTrace();
        }
    }

    public static void buildJsonResponse(HttpServletResponse response, BaseDto dto) {
        setContentType(response);
        try (PrintWriter writer = response.getWriter();) {
            writer.write(mapper.writeValueAsString(dto));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void buildJsonResponse(HttpServletResponse response, List<? extends BaseDto> dtoList) {
        setContentType(response);
        try (PrintWriter writer = response.getWriter();) {
            writer.write(mapper.writeValueAsString(dtoList));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    private static void setContentType(HttpServletResponse httpResponse) {
        httpResponse.setContentType("application/json");
        httpResponse.setCharacterEncoding("UTF-8");
    }
}

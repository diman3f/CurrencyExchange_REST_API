package org.pet.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.pet.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class ValidatorURLUtil {
    public static boolean hasValidCodeCurrencyFormat(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        return pathParts.length == 2;
    }

    public static boolean hasValidCurrencyPairFormat(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        return pathParts.length == 5;
    }

    public static List<String> getValidPairCurrencyPairFormat(String pathUrl) {
        String[] pathParts = pathUrl.split("/");
        if (pathUrl.length() > 1) {
            String currencyPair = pathParts[1];
            isValidLengthPairCurrencyCode(currencyPair);
            String baseCode = currencyPair.substring(0, 3);
            String targetCode = currencyPair.substring(3);
            List<String> code = List.of(baseCode, targetCode);
            return code;
        }
        throw new ValidationException("Длина валютной пары должна быть 6 символов из латинских заглавных букв");
    }


    private static void isValidLengthPairCurrencyCode(String currencyPair) {

        if (currencyPair == null || !currencyPair.matches("[A-Z]{6}")) {
            throw new ValidationException("Длина валютной пары должна быть 6 символов из латинских заглавных букв");
        }
    }

}

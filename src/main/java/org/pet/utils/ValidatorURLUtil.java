package org.pet.utils;

import org.pet.exception.ValidationException;

import java.util.List;

public class ValidatorURLUtil {

    private ValidatorURLUtil() {
        throw new ArrayStoreException("Нельзя создать объект статического класса");
    }

    public static List<String> getValidPairCurrencyPairFormat(String pathUrl) {

        String[] pathParts = pathUrl.split("/");
        if (hasMultipleFragments(pathUrl)) {
            String currencyPair = getPairCurrencyOfURLPath(pathParts);
            isValidLengthPairCurrencyCode(currencyPair);
            String baseCode = currencyPair.substring(0, 3);
            String targetCode = currencyPair.substring(3);
            List<String> code = List.of(baseCode, targetCode);
            return code;
        }
        throw new ValidationException("Длина валютной пары должна быть 6 символов из латинских заглавных букв");
    }

    private static boolean hasMultipleFragments(String pathUrl) {
        return pathUrl.length() > 1;
    }

    private static String getPairCurrencyOfURLPath(String[] pathParts) {
        return pathParts[1];
    }

    private static void isValidLengthPairCurrencyCode(String currencyPair) {
        if (currencyPair == null || !currencyPair.matches("[A-Z]{6}")) {
            throw new ValidationException("Длина валютной пары должна быть 6 символов из латинских заглавных букв");
        }
    }

}

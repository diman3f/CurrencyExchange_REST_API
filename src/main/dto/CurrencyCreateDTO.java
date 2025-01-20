package main.dto;

import main.CodeCurrency;

public class CurrencyCreateDTO {

    private final int id;
    private final String fullName;
    private final String code;


    public CurrencyCreateDTO(Integer id, String fullName, CodeCurrency currency) {
        this.id = id;
        this.fullName = fullName;
        this.code = currency.name();
    }

    public String getFullName() {
        return fullName;
    }

    public String getCode() {
        return code;
    }

    public int getId() {
        return id;
    }
}

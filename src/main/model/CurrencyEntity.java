package main.model;

import main.CodeCurrency;

public class CurrencyEntity {

    private Integer id;
    private String code;
    private String fullName;



    public CurrencyEntity(int id, String code, String fullName) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCode() {
        return code;
    }

    public void setCode(CodeCurrency currency) {
        this.code = currency.name();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}

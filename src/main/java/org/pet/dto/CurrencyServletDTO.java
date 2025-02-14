package org.pet.dto;

public class CurrencyServletDTO {

    private String code;
    private String full_name;
    private String sign;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "CurrencyDTO{" +
                "code='" + code + '\'' +
                ", full_name='" + full_name + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}

package org.pet.dto;

public class ExchangeRateDTO {
    private int id;
    private CurrencyDTO currencyDTOBase;
    private CurrencyDTO currencyDTOTarget;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CurrencyDTO getCurrencyDTOBase() {
        return currencyDTOBase;
    }

    public void setCurrencyDTOBase(CurrencyDTO currencyDTOBase) {
        this.currencyDTOBase = currencyDTOBase;
    }

    public CurrencyDTO getCurrencyDTOTarget() {
        return currencyDTOTarget;
    }

    public void setCurrencyDTOTarget(CurrencyDTO currencyDTOTarget) {
        this.currencyDTOTarget = currencyDTOTarget;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }

    @Override
    public String toString() {
        return "ExchangeRateDTO{" +
                "id=" + id +
                ", currencyDTOBase=" + currencyDTOBase +
                ", currencyDTOTarget=" + currencyDTOTarget +
                ", Rate=" + Rate +
                '}';
    }

    private double Rate;
}





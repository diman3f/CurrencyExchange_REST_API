package org.pet.exception;

public class ExchangeRateException extends CurrencyNotFoundException{
    public ExchangeRateException(String message) {
        super(message);
    }
}

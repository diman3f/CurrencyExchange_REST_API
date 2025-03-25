package org.pet.exception;

import java.net.http.HttpResponse;
import java.util.InputMismatchException;

public class ExceptionHandler extends Exception {
    private int code;
    private RuntimeException exception;

    public ExceptionHandler(RuntimeException e) {
        this.exception = e;
        setCodeException();
    }
    public int getCode() {
        return code;
    }
    private void setCodeException() {
        Class typeException = exception.getClass();
        if (typeException.equals(CurrencyException.class)) {
            code = 404;
        } else if (typeException.equals(InvalidParameterCurrencyException.class)) {
            code = 400;
        } else if (typeException.equals(DaoException.class)) {
            code = 500;
        }
    }
}

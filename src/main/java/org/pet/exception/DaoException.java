package org.pet.exception;

public class DaoException extends RuntimeException {
    public DaoException(String message) {
        super(message);
    }
    public DaoException(Exception e, String message) {
        super(message);
        e.printStackTrace();
    }

}

package org.pet.exception;

public class MissingRequiredValueException extends RuntimeException {

    public MissingRequiredValueException(String message) {
        super(message);
    }
}

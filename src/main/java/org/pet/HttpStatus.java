package org.pet;

import lombok.Getter;

@Getter
public enum HttpStatus {

    NOT_FOUND(404),
    CREATED (201);

    private final int code;


    HttpStatus(int code) {
        this.code = code;
    }

}

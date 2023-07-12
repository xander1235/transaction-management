package com.loco.transaction.management.exceptions;

import org.springframework.http.HttpStatus;

public class LocoGenericException extends LocoException {

    private final HttpStatus status;

    public LocoGenericException(String errorMsg, HttpStatus status) {
        super(errorMsg);
        this.status = status;
    }

    @Override
    public  HttpStatus getHttpStatus() {
        return this.status;
    }
}

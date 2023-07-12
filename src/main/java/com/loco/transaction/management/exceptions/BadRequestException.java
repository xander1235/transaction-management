package com.loco.transaction.management.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends LocoException {

    public BadRequestException(String errorMsg) {
        super(errorMsg);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}

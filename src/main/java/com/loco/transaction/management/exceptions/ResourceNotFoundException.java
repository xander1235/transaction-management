package com.loco.transaction.management.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends LocoException {

    public ResourceNotFoundException(String errorMsg) {
        super(errorMsg);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}

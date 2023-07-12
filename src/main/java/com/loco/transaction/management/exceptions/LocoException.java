package com.loco.transaction.management.exceptions;

import org.springframework.http.HttpStatus;

public abstract class LocoException extends RuntimeException {

    public LocoException(String errorMsg) {
        super(errorMsg);
    }

    public LocoException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }

    public LocoException(Throwable cause) {
        super(cause);
    }

    public abstract HttpStatus getHttpStatus();

}

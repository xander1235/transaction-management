package com.loco.transaction.management.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * All types of exceptions can be handled at one Location
 */
@RestControllerAdvice
@Slf4j
public class LocoExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles the generic exceptions
     *
     * @param ex  all the exception details
     * @param req It provides a way to access and manipulate various aspects of an incoming HTTP request in a web application
     * @return Custom error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGenericException(Exception ex, WebRequest req) {
        ErrorDetails error = getErrorDetails(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("Error occurred: {}", error);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * Handles all the exceptions related to LocoException
     *
     * @param ex  all the exception details
     * @param req It provides a way to access and manipulate various aspects of an incoming HTTP request in a web application
     * @return Custom error details
     */
    @ExceptionHandler(LocoException.class)
    public ResponseEntity<ErrorDetails> handleLocoException(LocoException ex, WebRequest req) {
        ErrorDetails error = getErrorDetails(ex.getMessage(), ex.getHttpStatus());
        log.error("Error occurred: {}", error);
        return new ResponseEntity<>(error, ex.getHttpStatus());
    }

    /**
     * Handles all the exceptions related to MethodArguments
     *
     * @param ex      all the exception details
     * @param headers HttpHeaders of the request
     * @param status  type of status related to error
     * @param request It provides a way to access and manipulate various aspects of an incoming HTTP request in a web application
     * @return the generic object
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        StringBuilder sb = new StringBuilder();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            sb.append(error.getDefaultMessage())
                    .append(";");

        }

        ErrorDetails error = getErrorDetails(sb.toString(), HttpStatus.valueOf(status.value()));

        log.error("Error occurred: {}", error);

        return new ResponseEntity<>(error, HttpStatus.valueOf(status.value()));
    }

    /**
     * Handles all the constraint violation exceptions like validating the method arguments might throw the exception
     *
     * @param ex      all the violated exception details
     * @param request It provides a way to access and manipulate various aspects of an incoming HTTP request in a web application
     * @return Custom error details
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorDetails> handleConstraintViolationException(
            ConstraintViolationException ex,
            WebRequest request) {

        StringBuilder sb = new StringBuilder();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            sb.append(violation.getMessage())
                    .append(";");
        }

        ErrorDetails error = getErrorDetails(sb.toString(), HttpStatus.BAD_REQUEST);

        log.error("Error occurred: {}", error);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gets the error details with given details
     *
     * @param errorMsg error message to be displayed
     * @param status   type of status related to error
     * @return ErrorDetails formed
     */
    private ErrorDetails getErrorDetails(String errorMsg, HttpStatus status) {
        return ErrorDetails.builder()
                .errorMessage(errorMsg)
                .status(status.name())
                .errorCode(status.value())
                .build();
    }

}

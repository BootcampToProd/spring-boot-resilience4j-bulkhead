package com.bootcamptoprod.bulkhead.exception;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalErrorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BulkheadFullException.class})
    protected ResponseEntity<Error> handleBulkheadFullException(BulkheadFullException ex, WebRequest request) {
        System.out.println("BulkheadFullException encountered: " + ex);
        Error Error = new Error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Error);
    }
}
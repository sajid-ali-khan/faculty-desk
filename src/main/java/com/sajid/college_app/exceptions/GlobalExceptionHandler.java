package com.sajid.college_app.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception e){
        return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Something went wrong.",
                "actualMessage", e.getMessage()
        ));
    }
}

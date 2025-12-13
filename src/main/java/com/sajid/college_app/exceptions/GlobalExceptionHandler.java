package com.sajid.college_app.exceptions;

import com.opencsv.exceptions.CsvValidationException;
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

    @ExceptionHandler(CsvValidationException.class)
    public ResponseEntity<?> handleCsvValidationException(CsvValidationException e){
        return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Invalid CSV file or CSV file not in required format.",
                "actualMessage", e.getMessage()
        ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e){
        return ResponseEntity.status(404).body(Map.of(
                "success", false,
                "message", e.getMessage()
        ));
    }
}

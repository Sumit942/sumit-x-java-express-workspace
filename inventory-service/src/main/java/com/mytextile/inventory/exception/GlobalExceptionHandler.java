package com.mytextile.inventory.exception;

import com.mytextile.inventory.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

// This class "advises" all Controllers on how to handle exceptions
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle our custom "Not Found" exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
                
        ErrorResponseDto error = new ErrorResponseDto(
            request.getDescription(false), // Gets the API path
            ex.getMessage(),
            HttpStatus.NOT_FOUND,
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Handle built-in Spring validation exceptions (from @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        // Get the first validation error message
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        ErrorResponseDto error = new ErrorResponseDto(
            request.getDescription(false),
            errorMessage,
            HttpStatus.BAD_REQUEST,
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
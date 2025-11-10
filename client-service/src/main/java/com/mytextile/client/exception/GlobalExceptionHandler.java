package com.mytextile.client.exception;

import com.mytextile.client.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

// This class "advises" all Controllers on how to handle exceptions
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle our custom "Not Found" exception
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
                
        return new ErrorResponseDto(
            request.getDescription(false), // Gets the API path
            ex.getMessage(),
            HttpStatus.NOT_FOUND,
            LocalDateTime.now()
        );
    }

    // Handle built-in Spring validation exceptions (from @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_GATEWAY)
    public ErrorResponseDto handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        // Get the first validation error message
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return new ErrorResponseDto(
            request.getDescription(false),
            errorMessage,
            HttpStatus.BAD_REQUEST,
            LocalDateTime.now()
        );
    }
}
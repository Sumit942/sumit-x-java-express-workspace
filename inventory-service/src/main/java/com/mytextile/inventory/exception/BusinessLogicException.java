package com.mytextile.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Use this when an action is forbidden by business rules
@ResponseStatus(value = HttpStatus.CONFLICT) // 409 Conflict
public class BusinessLogicException extends RuntimeException {
    public BusinessLogicException(String message) {
        super(message);
    }
}
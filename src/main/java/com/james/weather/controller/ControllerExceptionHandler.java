package com.james.weather.controller;

import com.james.weather.dto.ErrorDto;
import com.james.weather.exception.InvalidApikeyException;
import com.james.weather.exception.RateLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ErrorDto> handleBadRequestException(Exception exception){
        return new ResponseEntity(new ErrorDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidApikeyException.class})
    public ResponseEntity<ErrorDto> handleInvalidApikeyException(Exception exception){
        return new ResponseEntity(new ErrorDto(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({RateLimitExceededException.class})
    public ResponseEntity<ErrorDto> handleRateLimitException(Exception exception){
        return new ResponseEntity(new ErrorDto(exception.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
    }

}

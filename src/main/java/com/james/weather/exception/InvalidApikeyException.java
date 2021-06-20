package com.james.weather.exception;

public class InvalidApikeyException extends RuntimeException {
    public InvalidApikeyException() {
        super("Invalid Apikey");
    }
}
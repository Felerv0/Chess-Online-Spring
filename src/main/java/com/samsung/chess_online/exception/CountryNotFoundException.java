package com.samsung.chess_online.exception;

public class CountryNotFoundException extends RuntimeException{
    public CountryNotFoundException(String message) {
        super(message);
    }
}

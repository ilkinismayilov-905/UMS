package com.example.exception;

public class InvalidGradeValueException extends RuntimeException {

    public InvalidGradeValueException(String message) {
        super(message);
    }
}


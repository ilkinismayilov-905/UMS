package com.example.exception;

public class AbsenceLimitExceededException extends RuntimeException {

    public AbsenceLimitExceededException(String message) {
        super(message);
    }
}


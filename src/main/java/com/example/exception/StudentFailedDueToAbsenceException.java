package com.example.exception;

public class StudentFailedDueToAbsenceException extends RuntimeException {

    public StudentFailedDueToAbsenceException(String message) {
        super(message);
    }
}


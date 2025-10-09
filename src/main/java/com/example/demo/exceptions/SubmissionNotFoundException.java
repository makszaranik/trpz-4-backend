package com.example.demo.exceptions;

public class SubmissionNotFoundException extends RuntimeException {

    public SubmissionNotFoundException(String message) {
        super(message);
    }

}

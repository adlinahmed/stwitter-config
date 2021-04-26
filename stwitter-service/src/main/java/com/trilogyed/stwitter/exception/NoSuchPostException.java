package com.trilogyed.stwitter.exception;

public class NoSuchPostException extends RuntimeException{
    public NoSuchPostException(String message) {
        super(message);
    }
}

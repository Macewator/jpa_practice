package com.example.exception;

public class InvalidOptionException extends RuntimeException {
    public InvalidOptionException() {
        super("Opcja nie istnieje");
    }
}

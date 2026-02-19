package com.kce.util;

public class ActiveEngagementsExistException extends RuntimeException {

    public ActiveEngagementsExistException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "ActiveEngagementsExistException: " + getMessage();
    }
}

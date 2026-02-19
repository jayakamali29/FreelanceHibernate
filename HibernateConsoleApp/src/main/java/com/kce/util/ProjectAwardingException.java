package com.kce.util;

public class ProjectAwardingException extends RuntimeException {

    public ProjectAwardingException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "ProjectAwardingException: " + getMessage();
    }
}

package com.company.exception;

/**
 * Created by gunaas
 */
public class SwiggyException extends RuntimeException {

    public SwiggyException(String message) {
        super(message);
    }

    public SwiggyException(String message, Throwable cause) {
        super(message, cause);
    }
}

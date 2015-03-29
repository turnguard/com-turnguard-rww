package com.turnguard.rww.graphstore.rdf4j.backend.exception;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public class NoSuchRdf4JBackendException extends Exception {

    public NoSuchRdf4JBackendException() {
    }

    public NoSuchRdf4JBackendException(String message) {
        super(message);
    }

    public NoSuchRdf4JBackendException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchRdf4JBackendException(Throwable cause) {
        super(cause);
    }

    public NoSuchRdf4JBackendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

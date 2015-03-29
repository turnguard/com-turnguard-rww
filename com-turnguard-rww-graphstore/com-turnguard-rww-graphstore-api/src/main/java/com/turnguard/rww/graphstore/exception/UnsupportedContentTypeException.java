package com.turnguard.rww.graphstore.exception;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public class UnsupportedContentTypeException extends Exception {

    public UnsupportedContentTypeException() {
    }

    public UnsupportedContentTypeException(String message) {
        super(message);
    }

    public UnsupportedContentTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedContentTypeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedContentTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

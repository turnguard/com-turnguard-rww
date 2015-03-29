package com.turnguard.rww.graphstore.exception;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public class ContentTypeMismatchException extends Exception {

    public ContentTypeMismatchException() {
    }

    public ContentTypeMismatchException(String message) {
        super(message);
    }

    public ContentTypeMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentTypeMismatchException(Throwable cause) {
        super(cause);
    }

    public ContentTypeMismatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

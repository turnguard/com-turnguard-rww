package com.turnguard.rww.graphstore.exception;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public class GraphRestrictionException extends Exception {

    public GraphRestrictionException() {
    }

    public GraphRestrictionException(String message) {
        super(message);
    }

    public GraphRestrictionException(String message, Throwable cause) {
        super(message, cause);
    }

    public GraphRestrictionException(Throwable cause) {
        super(cause);
    }

    public GraphRestrictionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

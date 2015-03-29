package com.turnguard.rww.graphstore.exception;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public class GraphSyntaxException extends Exception {

    public GraphSyntaxException() {
    }

    public GraphSyntaxException(String message) {
        super(message);
    }

    public GraphSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public GraphSyntaxException(Throwable cause) {
        super(cause);
    }

    public GraphSyntaxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

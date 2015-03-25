package com.turnguard.rww.webid.exceptions;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class UnsupportedContentTypeException extends Exception {

    public UnsupportedContentTypeException(Throwable thrwbl) {
        super(thrwbl);
    }

    public UnsupportedContentTypeException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public UnsupportedContentTypeException(String string) {
        super(string);
    }

    public UnsupportedContentTypeException() {
    }
    
}

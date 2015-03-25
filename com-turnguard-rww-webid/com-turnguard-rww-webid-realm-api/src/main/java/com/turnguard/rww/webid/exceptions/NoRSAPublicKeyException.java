package com.turnguard.rww.webid.exceptions;

/**
 * An exception that is thrown in case no cert#key was found.
 * 
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class NoRSAPublicKeyException extends Exception {

    public NoRSAPublicKeyException(Throwable thrwbl) {
        super(thrwbl);
    }

    public NoRSAPublicKeyException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public NoRSAPublicKeyException(String string) {
        super(string);
    }

    public NoRSAPublicKeyException() {
    }
    
}

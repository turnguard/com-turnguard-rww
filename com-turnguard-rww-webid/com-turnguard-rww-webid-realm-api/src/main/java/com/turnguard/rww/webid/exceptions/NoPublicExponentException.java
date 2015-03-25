package com.turnguard.rww.webid.exceptions;

/**
 * An exception that is thrown in case no cert#exponent could be found at all.
 * 
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class NoPublicExponentException extends NoSuchPredicateException {

    public NoPublicExponentException() {
    }

    public NoPublicExponentException(String string) {
        super(string);
    }

    public NoPublicExponentException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public NoPublicExponentException(Throwable thrwbl) {
        super(thrwbl);
    }
    
}

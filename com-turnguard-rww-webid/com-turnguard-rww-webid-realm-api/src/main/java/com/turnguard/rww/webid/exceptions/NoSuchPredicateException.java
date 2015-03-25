package com.turnguard.rww.webid.exceptions;

/**
 * An exception that is thrown in case a predicate is queried from the internal 
 * structure of a WebIDPrincipal that doesn't exist.
 * 
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class NoSuchPredicateException extends Exception {

    public NoSuchPredicateException(Throwable thrwbl) {
        super(thrwbl);
    }

    public NoSuchPredicateException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public NoSuchPredicateException(String string) {
        super(string);
    }

    public NoSuchPredicateException() {
    }
    
}

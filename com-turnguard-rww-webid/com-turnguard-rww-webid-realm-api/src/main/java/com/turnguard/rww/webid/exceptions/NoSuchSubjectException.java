package com.turnguard.rww.webid.exceptions;

/**
 * An exception that is thrown in case a subject is queried from the internal 
 * structure of a WebIDPrincipal that doesn't exist.
 * 
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class NoSuchSubjectException extends Exception {

    public NoSuchSubjectException(Throwable thrwbl) {
        super(thrwbl);
    }

    public NoSuchSubjectException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public NoSuchSubjectException(String string) {
        super(string);
    }

    public NoSuchSubjectException() {
    }
    
}

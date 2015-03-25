package com.turnguard.rww.webid.exceptions;

/**
 * An exception that is thrown in case no cert#modulus at all could be found
 * 
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class NoModulusException extends NoSuchPredicateException {

    public NoModulusException() {
    }

    public NoModulusException(String string) {
        super(string);
    }

    public NoModulusException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public NoModulusException(Throwable thrwbl) {
        super(thrwbl);
    }
    
}

package com.turnguard.rww.webid.exceptions;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class PublicExponentMismatchException extends Exception {

    public PublicExponentMismatchException(Throwable thrwbl) {
        super(thrwbl);
    }

    public PublicExponentMismatchException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public PublicExponentMismatchException(String string) {
        super(string);
    }

    public PublicExponentMismatchException() {
    }
    
}

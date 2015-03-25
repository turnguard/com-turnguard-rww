package com.turnguard.rww.webid.exceptions;

/**
 * Exception that is thrown in case a WebIDClaim is determined to be an URL 
 * as opposed to an URI. This is the case no redirect takes place and the server
 * answers with 200 OK.
 * 
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class DereferencingException extends Exception {

    public DereferencingException(Throwable thrwbl) {
        super(thrwbl);
    }

    public DereferencingException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public DereferencingException(String string) {
        super(string);
    }

    public DereferencingException() {
    }
    
}

package com.turnguard.rww.webid.exceptions;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class NoWebIDFoundException extends Exception {

    public NoWebIDFoundException(Throwable thrwbl) {
        super(thrwbl);
    }

    public NoWebIDFoundException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public NoWebIDFoundException(String string) {
        super(string);
    }

    public NoWebIDFoundException() {
    }
    
}

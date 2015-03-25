package com.turnguard.rww.webid.exceptions;

/**
 * An exception that is thrown when a matching cert#exponent was found
 * but no matching cert#modulus could be established
 * 
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class ModulusMismatchException extends Exception {
    /**
     * 
     * @param thrwbl 
     */
    public ModulusMismatchException(Throwable thrwbl) {
        super(thrwbl);
    }
    /**
     * 
     * @param string
     * @param thrwbl 
     */
    public ModulusMismatchException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    /**
     * 
     * @param string 
     */
    public ModulusMismatchException(String string) {
        super(string);
    }

    /**
     * 
     */
    public ModulusMismatchException() {
    }
    
}

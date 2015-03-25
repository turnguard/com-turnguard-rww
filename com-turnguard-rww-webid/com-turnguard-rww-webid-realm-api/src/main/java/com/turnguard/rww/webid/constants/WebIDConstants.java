package com.turnguard.rww.webid.constants;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class WebIDConstants {    
    public static enum MODE {
        /**
         * If WebIDDatabase is run in this mode, it will 
         * try to dereference WebIDClaims over http.
         */
        DEREFERENCE_ONLY,
        /**
         * If WebIDDatabase is run in this mode, it will
         * only look for WebIDClaim statements in the 
         * underlying repository.
         */
        DEREFERENCE_NO
    }
}

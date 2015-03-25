package com.turnguard.rww.webid.vocabulary;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class CERT {
    /** http://www.w3.org/ns/auth/cert# */
    public static final String NAMESPACE = "http://www.w3.org/ns/auth/cert#";        
    
    public static final URI KEY;
    public static final URI EXPONENT;
    public static final URI MODULUS;
    
    
    static {
        ValueFactory factory = ValueFactoryImpl.getInstance();
        KEY = factory.createURI(NAMESPACE, "key");    
        EXPONENT = factory.createURI(NAMESPACE, "exponent");    
        MODULUS = factory.createURI(NAMESPACE, "modulus");            
    }
}

package com.turnguard.rww.webid.vocabulary;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class WEBID {
    /** http://schema.turnguard.com/webid/2.0/core# */
    public static final String NAMESPACE = "http://schema.turnguard.com/webid/2.0/core#";
    
    public static final URI ROLE;
    public static final URI GROUP;
    public static final URI USER;
    
    public static final URI HAS_ROLE;
    public static final URI HAS_GROUP;
    public static final URI HAS_USER;
    
    
    static {
        ValueFactory factory = ValueFactoryImpl.getInstance();
        ROLE = factory.createURI(NAMESPACE, "Role");    
        GROUP = factory.createURI(NAMESPACE, "Group");    
        USER = factory.createURI(NAMESPACE, "User");    
        HAS_ROLE = factory.createURI(NAMESPACE, "hasRole");    
        HAS_GROUP = factory.createURI(NAMESPACE, "hasGroup");    
        HAS_USER = factory.createURI(NAMESPACE, "hasUser");            
    }
    
    public static class DATA {
        /** http://data.turnguard.com/webid/2.0/ */
        public static final String NAMESPACE = "http://data.turnguard.com/webid/2.0/";        
        
        public static final URI ROLE_VOID;        
        static {
            ValueFactory factory = ValueFactoryImpl.getInstance();
            ROLE_VOID = factory.createURI(NAMESPACE, "Void");            
        }        
    }
}

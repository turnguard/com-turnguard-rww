package com.turnguard.rww.webid.vocabulary;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class JAVA {
    
    public static final String NAMESPACE = "http://data.turnguard.com/java/1.6.0_29/";
    
    public static URI getURI(Class clazz){
        return new URIImpl(NAMESPACE+(clazz.getName().replaceAll("\\.", "/")));
    }
    
    public static class KEYWORD {
        /** http://schema.turnguard.com/java/1.6.0_29/core/ */
        public static final String NAMESPACE = "http://schema.turnguard.com/java/1.6.0_29/core/";    
        
        public static URI THROWS;
        static {
            ValueFactory factory = ValueFactoryImpl.getInstance();
            THROWS = factory.createURI(NAMESPACE, "throws");            
        }        
    }
    
    public static class DATA {
        /** http://data.turnguard.com/java/1.6.0_29/core/ */
        public static final String NAMESPACE = "http://data.turnguard.com/java/1.6.0_29/core/";    
        
        public static URI MESSAGE;
        static {
            ValueFactory factory = ValueFactoryImpl.getInstance();
            MESSAGE = factory.createURI(NAMESPACE, "message");             
        }        
    }    
    
    public static class LANG {
        /** http://schema.turnguard.com/java/1.0/java/lang/ */
        public static final String NAMESPACE = "http://schema.turnguard.com/java/1.6.0_29/java/lang/";
        
        public static final URI EXCEPTION;        
        
        public static final URI MESSAGE;
        static {
            ValueFactory factory = ValueFactoryImpl.getInstance();
            EXCEPTION = factory.createURI(NAMESPACE, "Exception");    
            MESSAGE = factory.createURI(NAMESPACE, "message");    
        }
    }

}

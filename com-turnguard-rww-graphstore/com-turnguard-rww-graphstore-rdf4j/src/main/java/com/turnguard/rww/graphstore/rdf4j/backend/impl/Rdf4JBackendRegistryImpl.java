package com.turnguard.rww.graphstore.rdf4j.backend.impl;

import com.turnguard.rww.graphstore.rdf4j.backend.Rdf4JBackend;
import com.turnguard.rww.graphstore.rdf4j.backend.Rdf4JBackendRegistry;
import com.turnguard.rww.graphstore.rdf4j.backend.exception.NoSuchRdf4JBackendException;
import java.util.Iterator;
import java.util.ServiceLoader;
import javax.servlet.ServletConfig;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public class Rdf4JBackendRegistryImpl implements Rdf4JBackendRegistry {
    
    private static Rdf4JBackendRegistry service;
    private final ServiceLoader<Rdf4JBackend> loader;
    
    public Rdf4JBackendRegistryImpl(){
        loader = ServiceLoader.load(Rdf4JBackend.class);        
    }
    
    public static synchronized Rdf4JBackendRegistry getInstance() {
        if (service == null) {
            service = new Rdf4JBackendRegistryImpl();
        }
        return service;
    }
    
    @Override
    public Repository createRepository(Class clazz, ServletConfig config) throws NoSuchRdf4JBackendException, RepositoryException{
        Rdf4JBackend backend;
        for( Iterator<Rdf4JBackend> iter = this.loader.iterator(); iter.hasNext(); ){
            backend = iter.next();
            if(backend.getBackendClass().equals(clazz)){
                return backend.createRepository(config);
            }
        } 
        throw new NoSuchRdf4JBackendException();
    }
    
}

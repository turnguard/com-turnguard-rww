package com.turnguard.rww.graphstore.rdf4j.backend;

import javax.servlet.ServletConfig;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public interface Rdf4JBackend<T> {
    public Class<T> getBackendClass(); 
    public Repository createRepository(ServletConfig config) throws RepositoryException;
}

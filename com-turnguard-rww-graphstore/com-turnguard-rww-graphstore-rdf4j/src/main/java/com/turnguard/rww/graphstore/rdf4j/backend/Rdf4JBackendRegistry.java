package com.turnguard.rww.graphstore.rdf4j.backend;

import com.turnguard.rww.graphstore.rdf4j.backend.exception.NoSuchRdf4JBackendException;
import javax.servlet.ServletConfig;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public interface Rdf4JBackendRegistry {
    public Repository createRepository(Class clazz, ServletConfig config) throws NoSuchRdf4JBackendException, RepositoryException;
}

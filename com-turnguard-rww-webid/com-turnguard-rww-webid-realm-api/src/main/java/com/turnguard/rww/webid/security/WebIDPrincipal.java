package com.turnguard.rww.webid.security;

import com.turnguard.rww.webid.exceptions.NoSuchPredicateException;
import com.turnguard.rww.webid.exceptions.NoSuchSubjectException;
import java.security.Principal;
import java.util.HashSet;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public interface WebIDPrincipal extends Principal {
    public Resource getURI();
    public HashSet<Value> get(URI predicate) throws NoSuchPredicateException;
    public HashSet<Value> get(Resource subject, URI predicate) throws NoSuchSubjectException, NoSuchPredicateException;
}

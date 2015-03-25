package com.turnguard.rww.webid.security.base;

import com.turnguard.rww.webid.exceptions.NoSuchPredicateException;
import com.turnguard.rww.webid.exceptions.NoSuchSubjectException;
import com.turnguard.rww.webid.security.WebIDPrincipal;
import com.turnguard.rww.webid.utils.WebIDUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class WebIDPrincipalBase implements WebIDPrincipal {
    protected Resource uri;
    protected HashMap<Resource,HashMap<URI,HashSet<Value>>> principalData;

    public WebIDPrincipalBase(Resource uri) {
        this.uri = uri;
        this.principalData = new HashMap<Resource,HashMap<URI,HashSet<Value>>>();
    }
    
    public WebIDPrincipalBase(Resource uri, Collection<Statement> states) {
        this.uri = uri;
        this.principalData = WebIDUtils.SailUtils.toHashMap(states);
    }
    
    @Override
    public Resource getURI() {
        return this.uri;
    }
    
    @Override
    public HashSet<Value> get(URI predicate) throws NoSuchPredicateException {
        if(this.principalData.containsKey(uri) && this.principalData.get(uri).containsKey(predicate)){
            return this.principalData.get(uri).get(predicate);
        }
        throw new NoSuchPredicateException("Data of " + uri + " doesn't contain " + predicate);
    }    
    
    @Override
    public HashSet<Value> get(Resource subject, URI predicate) throws NoSuchSubjectException, NoSuchPredicateException {
        if(!this.principalData.containsKey(subject)){
            throw new NoSuchSubjectException("Data of " + uri + " doesn't contain anything about " + subject);
        } else {
            if(!this.principalData.get(subject).containsKey(predicate)){
                throw new NoSuchPredicateException("Data of " + subject + " doesn't contain " + predicate);
            } else {
                return this.principalData.get(subject).get(predicate);
            }
        }
    }
    
    @Override
    public String getName() {
        return uri.stringValue();
    }

    @Override
    public String toString() {
        return this.uri.stringValue();
    }

}

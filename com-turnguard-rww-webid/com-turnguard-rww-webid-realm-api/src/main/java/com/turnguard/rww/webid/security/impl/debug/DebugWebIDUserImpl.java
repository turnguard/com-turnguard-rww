package com.turnguard.rww.webid.security.impl.debug;

import com.turnguard.rww.webid.exceptions.DereferencingException;
import com.turnguard.rww.webid.security.impl.WebIDUserImpl;
import java.util.Collection;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class DebugWebIDUserImpl extends WebIDUserImpl {

    public DebugWebIDUserImpl(Resource uri, Collection<Statement> states) throws DereferencingException {
        super(uri, states);
    }
        
    public DebugWebIDUserImpl(Resource uri) {
        super(uri);
    }   
}

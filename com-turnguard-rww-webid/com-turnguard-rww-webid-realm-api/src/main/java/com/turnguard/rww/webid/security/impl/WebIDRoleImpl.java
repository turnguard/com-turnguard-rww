package com.turnguard.rww.webid.security.impl;

import com.turnguard.rww.webid.security.WebIDRole;
import com.turnguard.rww.webid.security.base.WebIDPrincipalBase;
import org.openrdf.model.Resource;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class WebIDRoleImpl extends WebIDPrincipalBase implements WebIDRole {

    public WebIDRoleImpl(Resource uri) {
        super(uri);
    }
    
}

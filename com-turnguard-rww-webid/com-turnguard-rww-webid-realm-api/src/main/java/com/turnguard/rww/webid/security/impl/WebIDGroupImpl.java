package com.turnguard.rww.webid.security.impl;

import com.turnguard.rww.webid.security.WebIDGroup;
import com.turnguard.rww.webid.security.base.WebIDPrincipalBase;
import org.openrdf.model.Resource;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class WebIDGroupImpl extends WebIDPrincipalBase implements WebIDGroup {

    public WebIDGroupImpl(Resource uri) {
        super(uri);
    }
    
}

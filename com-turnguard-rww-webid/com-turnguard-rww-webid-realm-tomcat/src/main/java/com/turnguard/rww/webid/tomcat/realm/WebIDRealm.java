package com.turnguard.rww.webid.tomcat.realm;

import com.turnguard.rww.webid.database.WebIDDatabase;
import com.turnguard.rww.webid.security.WebIDPrincipal;
import com.turnguard.rww.webid.security.impl.WebIDRoleImpl;
import java.io.IOException;
import java.security.Principal;
import java.security.cert.X509Certificate;
import javax.naming.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.deploy.SecurityConstraint;
import org.apache.catalina.realm.RealmBase;
import org.apache.log4j.Logger;
import org.openrdf.model.impl.URIImpl;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class WebIDRealm extends RealmBase {
    private final static Logger logger = Logger.getLogger(WebIDRealm.class);
    private WebIDDatabase database;
    private String resourceName;

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    protected String getName() {
        logger.debug("WebIDRealm getName");
        return this.resourceName;
    }

    @Override
    protected String getPassword(String string) {
        logger.debug("WebIDRealm getPassword");
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Principal getPrincipal(String string) {
        logger.debug("WebIDRealm getPrincipal from String");
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Principal getPrincipal(X509Certificate usercert) {
        logger.debug("WebIDRealm getPrincipal from Certificate");
        return this.database.getPrincipal(usercert);
    }

    @Override
    public void startInternal() throws LifecycleException {
        logger.debug("WebIDRealm start");
        super.startInternal();
        try {
            Context context = super.getServer().getGlobalNamingContext();
            database = (WebIDDatabase) context.lookup(resourceName);
            logger.debug("WebIDRealm start, got database " + database);
        } catch (Throwable e) {
            logger.fatal(e);
            containerLog.error(sm.getString("userDatabaseRealm.lookup", resourceName), e);
            database = null;
        }
        if (database == null) {
            throw new LifecycleException(sm.getString("userDatabaseRealm.noDatabase", resourceName));
        }
        database.open();
    }

    @Override
    public void stopInternal() throws LifecycleException {
        logger.debug("WebIDRealm stop");
        super.stopInternal();
        database.close();
    }


    @Override
    public boolean hasUserDataPermission(Request request, Response response, SecurityConstraint[] constraints) throws IOException {
        return super.hasUserDataPermission(request, response, constraints);
    }
    @Override
    public boolean hasRole(Wrapper wrapper, Principal principal, String role) {
        logger.debug("WebIDRealm hasRole " + principal + " " + role);
        return this.database.hasRole((WebIDPrincipal)principal, new WebIDRoleImpl(new URIImpl(role)));
    }

    @Override
    public Principal authenticate(X509Certificate[] certs) {
        logger.debug("WebIDRealm authenticate");
        return super.authenticate(certs);
    }

}

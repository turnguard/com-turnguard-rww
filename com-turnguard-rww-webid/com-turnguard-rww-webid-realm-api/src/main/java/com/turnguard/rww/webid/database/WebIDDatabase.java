package com.turnguard.rww.webid.database;

import com.turnguard.rww.webid.constants.WebIDConstants;
import com.turnguard.rww.webid.security.WebIDPrincipal;
import java.security.cert.X509Certificate;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public interface WebIDDatabase {
    /**
     * Checks to see if a WebIDPrincipal is in a certain WebIDRole
     * @param principal
     * @param role
     * @return true is the WebIDPrincipal has the given role
     */
    public boolean hasRole(WebIDPrincipal principal, WebIDPrincipal role);
    /**
     * Retrieve the WebIDPrincipal by means of dereferencing the WebIDClaim
     * in the X509Certificate
     * @param usercert
     * @return a WebIDPrincipal or a DebugWebIDUserImpl (which is a WebIDPrincipal)
     * in case any exception occured. Please note that this behavior is for debugging
     * only and if you want to tell the client what exactly went wrong. It is also
     * ok to rewrite this behavior to return null in case of error.
     */
    public WebIDPrincipal getPrincipal(X509Certificate usercert);
    /**
     * Retrieves the mode. DEREFERENCE_YES means that the WebIDClaim
     * is dereferenced via http. DEREFERENCE_NO means the WebIDClaim
     * is search in the underlying sailConnection. Setting the userGraph
     * is strongly recommended in this mode.
     * @return the mode in which WebIDUserDatabase runs
     */
    public WebIDConstants.MODE getDereferenceMode();
    /**
     * Sets the DereferenceMode
     * @param mode 
     */
    public void setDereferenceMode(WebIDConstants.MODE mode);
    /**
     * Sets the graph in which the role statements are found
     * @param roleGraph 
     */
    public void setRoleGraph(String roleGraph);
    /**
     * Sets the graph in which the user statements are found
     * @param userGraph 
     */
    public void setUserGraph(String userGraph);
    /**
     * Opens the WebIDDataBase
     */
    public void open();
    /**
     * Closes the WebIDDataBase, closing the open readConncetion
     * and finally calling the closeWebIDDatabase method of the WebIDDatabaseFactory 
     * that create the WebIDDataBase.
     */
    public void close();
}

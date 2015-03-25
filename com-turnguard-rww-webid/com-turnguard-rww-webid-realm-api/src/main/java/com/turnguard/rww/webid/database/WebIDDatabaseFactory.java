package com.turnguard.rww.webid.database;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public interface WebIDDatabaseFactory {
    /**
     * Creates a WebIDDatabase from normal JNDI settings
     * @param o
     * @param name
     * @param cntxt
     * @param hshtbl
     * @return the WebIDDatabase
     */
    public WebIDDatabase openWebIDDatabase(Object o, Name name, Context cntxt, Hashtable<?, ?> hshtbl);
    /**
     * Closes the WebIDDatabase. This method is called by the WebIDDatabase's close method,
     * which in return is call by the container.
     */
    public void closeWebIDDatabase();
}

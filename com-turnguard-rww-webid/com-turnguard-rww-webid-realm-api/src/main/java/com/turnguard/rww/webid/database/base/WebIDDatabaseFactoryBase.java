package com.turnguard.rww.webid.database.base;

import com.turnguard.rww.webid.constants.WebIDConstants;
import com.turnguard.rww.webid.database.WebIDDatabase;
import com.turnguard.rww.webid.database.WebIDDatabaseFactory;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public abstract class WebIDDatabaseFactoryBase implements ObjectFactory, WebIDDatabaseFactory {
    
    private WebIDDatabase database = null;
    
    @Override
    public final Object getObjectInstance(Object obj, Name name, Context cntxt, Hashtable<?, ?> hshtbl) throws Exception {
        if ((obj == null) || !(obj instanceof Reference)) {
            return (null);
        }
        /* TODO
        Reference ref = (Reference) obj;
        if (!"org.apache.catalina.UserDatabase".equals(ref.getClassName())) {
            return (null);
        } 
         * 
         */        
        this.database = this.openWebIDDatabase(obj, name, cntxt, hshtbl);
        
        Reference ref = (Reference) obj;
        RefAddr ra = ref.get("mode");
        if (ra != null) {
            String mode = ra.getContent().toString();
            this.database.setDereferenceMode(WebIDConstants.MODE.valueOf(mode));
        }
        RefAddr roleGraph = ref.get("roleGraph");
        if (roleGraph != null) {                
            this.database.setRoleGraph(roleGraph.getContent().toString());
        }
        RefAddr userGraph = ref.get("userGraph");
        if (userGraph != null) {                        
            this.database.setUserGraph(userGraph.getContent().toString());
        }        
        return this.database;
    }
    
    @Override
    public abstract WebIDDatabase openWebIDDatabase(Object o, Name name, Context cntxt, Hashtable<?, ?> hshtbl);
    @Override
    public abstract void closeWebIDDatabase();
}

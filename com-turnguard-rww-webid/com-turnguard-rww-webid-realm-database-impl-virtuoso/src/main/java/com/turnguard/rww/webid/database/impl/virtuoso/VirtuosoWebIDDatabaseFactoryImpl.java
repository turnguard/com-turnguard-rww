package com.turnguard.rww.webid.database.impl.virtuoso;

import com.turnguard.rww.webid.database.WebIDDatabase;
import com.turnguard.rww.webid.database.base.WebIDDatabaseFactoryBase;
import com.turnguard.rww.webid.database.impl.WebIDDatabaseImpl;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import virtuoso.sesame2.driver.VirtuosoRepository;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class VirtuosoWebIDDatabaseFactoryImpl extends WebIDDatabaseFactoryBase {
    
    Repository myRepository;
    
    @Override
    public WebIDDatabase openWebIDDatabase(Object o, Name name, Context cntxt, Hashtable<?, ?> hshtbl) {
        Reference ref = (Reference) o;
        
        String host = null;
        String port = null;
        String user = "";
        String pass = "";
        
        RefAddr raHost = ref.get("host");
        if (raHost != null) {
            host = raHost.getContent().toString();            
        }   
        RefAddr raPort = ref.get("port");
        if (raPort != null) {
            port = raPort.getContent().toString();            
        }       
        RefAddr raUser = ref.get("user");
        if (raUser != null) {
            user = raUser.getContent().toString();            
        }       
        RefAddr raPass = ref.get("pass");
        if (raPass != null) {
            pass = raPass.getContent().toString();            
        }               
        String connectString = "jdbc:virtuoso://"+host;
               if(port!=null){
                   connectString += ":"+port;
               }
        myRepository = new VirtuosoRepository(connectString,user,pass);
        return new WebIDDatabaseImpl(myRepository);
    }

    @Override
    public void closeWebIDDatabase() {
        try {
            myRepository.shutDown();
        } catch (RepositoryException ex) {            
        }
    }
    
}

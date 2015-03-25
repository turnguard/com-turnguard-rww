package com.turnguard.rww.webid.database.impl.openrdf;

import com.turnguard.rww.webid.database.WebIDDatabase;
import com.turnguard.rww.webid.database.base.WebIDDatabaseFactoryBase;
import com.turnguard.rww.webid.database.impl.WebIDDatabaseImpl;
import java.io.File;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import org.apache.log4j.Logger;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.Sail;
import org.openrdf.sail.memory.MemoryStore;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class MemoryStoreFactory extends WebIDDatabaseFactoryBase {
    private final static Logger logger = Logger.getLogger(MemoryStoreFactory.class);
    
    private Sail memoryStore = new MemoryStore();
    private SailRepository repository = new SailRepository(memoryStore);
        
    @Override
    public WebIDDatabase openWebIDDatabase(Object obj, Name name, Context cntxt, Hashtable<?, ?> hshtbl) {
        logger.debug("getRepository");
        Reference ref = (Reference) obj;
        RefAddr ra = ref.get("pathname");
        if (ra != null) {
            String pathName = ra.getContent().toString();
            File file = new File(pathName);
            if (!file.isAbsolute()) {
                file = new File(System.getProperty("catalina.base"), pathName);
            }
            if(file.exists() && file.canRead()){
                try {
                    repository.initialize();
                    RepositoryConnection repCon = null;
                    try {
                        repCon = repository.getConnection();
                        repCon.setAutoCommit(false);
                        repCon.add(file, "http://schema.turnguard.com/webid/2.0/core#", RDFFormat.RDFXML);
                        repCon.commit();
                        logger.debug("imported roles from " + file.getAbsolutePath());
                    } catch(Exception e){
                        e.printStackTrace();
                    } finally {
                        if (repCon != null) {
                            repCon.close();
                        }
                    }                       
                } catch (RepositoryException ex) {
                    logger.error(ex);
                }                    
            }
        }                            
        return new WebIDDatabaseImpl(repository);
    }

    @Override
    public void closeWebIDDatabase() {        
        try {
            logger.debug("closing WebIDDatabase");
            this.repository.shutDown();
        } catch (RepositoryException ex) {     
            logger.error(ex);
        }
    }
    
}

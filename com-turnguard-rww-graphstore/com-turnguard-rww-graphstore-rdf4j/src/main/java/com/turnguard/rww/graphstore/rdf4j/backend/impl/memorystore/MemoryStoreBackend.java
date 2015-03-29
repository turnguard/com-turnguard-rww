package com.turnguard.rww.graphstore.rdf4j.backend.impl.memorystore;

import com.turnguard.rww.graphstore.rdf4j.backend.Rdf4JBackend;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public class MemoryStoreBackend implements Rdf4JBackend<MemoryStore>{

    private final String SETTING_PARENT_DIRECTORY = "parentDirectory";
    @Override
    public Class<MemoryStore> getBackendClass() {
        return MemoryStore.class;
    }

    @Override
    public Repository createRepository(ServletConfig config) throws RepositoryException {
        MemoryStore store = new MemoryStore();
        if(config.getInitParameter(SETTING_PARENT_DIRECTORY)!=null){
            File f = new File(config.getInitParameter(SETTING_PARENT_DIRECTORY));
            if(!f.isDirectory() || !f.exists() || !f.canRead() || !f.canWrite()){
                throw new RepositoryException("Cannot create Repository in " + config.getInitParameter(SETTING_PARENT_DIRECTORY));
            }
            store.setDataDir(f);
            store.setPersist(true);
        }
        Repository repository = new SailRepository(store);            
                   repository.initialize();
        return repository;
    }


}

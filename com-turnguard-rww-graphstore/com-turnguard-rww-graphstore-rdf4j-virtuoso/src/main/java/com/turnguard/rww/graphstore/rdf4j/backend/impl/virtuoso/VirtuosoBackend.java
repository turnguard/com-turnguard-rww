package com.turnguard.rww.graphstore.rdf4j.backend.impl.virtuoso;

import com.turnguard.rww.graphstore.rdf4j.backend.Rdf4JBackend;
import javax.servlet.ServletConfig;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import virtuoso.sesame2.driver.VirtuosoRepository;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public class VirtuosoBackend implements Rdf4JBackend<VirtuosoRepository> {
    
    private final String PARAM_VIRTUOSO_HOST = "virtuoso.host";
    private final String PARAM_VIRTUOSO_PORT = "virtuoso.port";
    private final String PARAM_VIRTUOSO_USER = "virtuoso.user";
    private final String PARAM_VIRTUOSO_PASS = "virtuoso.pass";

    @Override
    public Class<VirtuosoRepository> getBackendClass() {
        return VirtuosoRepository.class;
    }

    @Override
    public Repository createRepository(ServletConfig config) throws RepositoryException {
        String host = config.getInitParameter(PARAM_VIRTUOSO_HOST);
        if(host==null){ throw new RepositoryException("Missing servlet init-parameter: "+PARAM_VIRTUOSO_HOST); }
        String port = config.getInitParameter(PARAM_VIRTUOSO_PORT);
        if(port==null){ port="1111"; }
        String user = config.getInitParameter(PARAM_VIRTUOSO_USER);
        if(user==null){ port="dba"; }
        String pass = config.getInitParameter(PARAM_VIRTUOSO_PASS);
        if(pass==null){ port="dba"; }        
        String defaultGraph = config.getInitParameter("default.graph");
        if(defaultGraph==null){
            throw new RepositoryException("VirtuosoRepository doesn't work without default graph");
        }
        host = host.concat(":").concat(port);
        
        return new VirtuosoRepository(host,user,pass,defaultGraph);
    }

}

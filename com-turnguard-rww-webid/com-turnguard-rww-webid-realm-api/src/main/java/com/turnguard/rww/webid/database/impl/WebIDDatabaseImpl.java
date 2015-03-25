package com.turnguard.rww.webid.database.impl;

import com.turnguard.rww.webid.constants.WebIDConstants;
import com.turnguard.rww.webid.constants.WebIDConstants.MODE;
import com.turnguard.rww.webid.exceptions.DereferencingException;
import com.turnguard.rww.webid.exceptions.ModulusMismatchException;
import com.turnguard.rww.webid.exceptions.NoModulusException;
import com.turnguard.rww.webid.exceptions.NoPublicExponentException;
import com.turnguard.rww.webid.exceptions.NoRSAPublicKeyException;
import com.turnguard.rww.webid.exceptions.NoSuchSubjectException;
import com.turnguard.rww.webid.exceptions.NoWebIDFoundException;
import com.turnguard.rww.webid.exceptions.PublicExponentMismatchException;
import com.turnguard.rww.webid.exceptions.UnsupportedContentTypeException;
import com.turnguard.rww.webid.database.WebIDDatabase;
import com.turnguard.rww.webid.database.WebIDDatabaseFactory;
import com.turnguard.rww.webid.security.WebIDPrincipal;
import com.turnguard.rww.webid.security.WebIDUser;
import com.turnguard.rww.webid.security.base.WebIDPrincipalBase;
import com.turnguard.rww.webid.security.impl.WebIDUserImpl;
import com.turnguard.rww.webid.security.impl.debug.DebugWebIDUserImpl;
import com.turnguard.rww.webid.utils.WebIDUtils;
import com.turnguard.rww.webid.vocabulary.JAVA;
import com.turnguard.rww.webid.vocabulary.WEBID;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.net.ssl.SSLHandshakeException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.openrdf.model.BNode;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import sun.security.rsa.RSAPublicKeyImpl;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class WebIDDatabaseImpl implements WebIDDatabase {

    private WebIDDatabaseFactory factory = null;
    private final static Logger logger = Logger.getLogger(WebIDDatabaseImpl.class);
    private Repository repository;
    private RepositoryConnection readConnection;
    private Resource[] roleGraph = new Resource[]{null};
    private Resource[] userGraph = new Resource[]{null};
    private WebIDConstants.MODE mode = null;
    
    public WebIDDatabaseImpl(Repository repository) {
        this.repository = repository;
        
    }
    
    public WebIDDatabaseImpl(Repository repository, String roleGraph) {
        this.repository = repository;        
        if(roleGraph!=null){
            this.roleGraph = new Resource[]{ new URIImpl(roleGraph) };
            logger.debug("Using RoleGraph " + roleGraph);
        }
    }
    
    public WebIDDatabaseImpl(SailRepository repository, String roleGraph, String userGraph) {
        this.repository = repository;        
        if(roleGraph!=null){
            this.roleGraph = new Resource[]{ new URIImpl(roleGraph) };
            logger.debug("Using RoleGraph " + roleGraph);
        }
        if(userGraph!=null){
            this.userGraph = new Resource[]{ new URIImpl(userGraph) };
            logger.debug("Using UserGraph " + userGraph);
        }
    }
    
        
    @Override
    public void open() {
        try {
            try {
                this.repository.initialize();
            } catch(IllegalStateException i){
                logger.warn("Repository is already initialized");
            }
            this.readConnection = this.repository.getConnection();
            logger.debug("Checking Connection");
            List<Statement> roleGraphStates = this.readConnection.getStatements(null, null, null, true, roleGraph).asList();
            logger.debug("RoleGraphSize " + roleGraphStates.size());
            roleGraphStates.clear();
        } catch (RepositoryException ex) {
            logger.fatal(ex.getMessage(), ex);
        }        
    }

    @Override
    public void close() {
        try {         
            logger.debug("closing WebIDDatabase");
            this.readConnection.close();
        } catch (RepositoryException ex) {
            logger.warn(ex.getMessage(), ex);
        }     
        this.factory.closeWebIDDatabase();
    }    
    
    public final <T extends WebIDPrincipalBase> Iterator<T> getWebIDPrincipal(GraphQueryResult result, Class<T> s){        
        List<T> principals = new ArrayList<T>();        
        try {            
            while(result.hasNext()){
                Statement state = result.next();
                T principal = s.getDeclaredConstructor(Resource.class).newInstance(state.getSubject());
                  principals.add(principal);
            }                                      
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } 
        return principals.iterator();
    }
    
    
    @Override
    public boolean hasRole(WebIDPrincipal principal, WebIDPrincipal role){
        if(role.getURI().equals(WEBID.DATA.ROLE_VOID)){
            return true;
        }
        try {
            logger.debug("Checking role " + role.getURI().toString() + " of " + principal.getURI().stringValue());
            return this.readConnection.hasStatement(principal.getURI(), WEBID.HAS_ROLE, role.getURI(), true, this.roleGraph);
        } catch (RepositoryException ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }    

    @Override
    public WebIDPrincipal getPrincipal(X509Certificate usercert) {        
        org.openrdf.model.URI dummyURI = new URIImpl("urn:uuid:"+UUID.randomUUID().toString());        
        Collection<URI> webIDClaims = null;
        Collection<Statement> webIDStates = new ArrayList<Statement>();
        
        try {
            usercert.checkValidity();
            try {
                webIDClaims = WebIDUtils.CertificateUtils.getWebIDClaims(usercert);
                for(URI webIDClaim : webIDClaims){
                    try {
                        Collection<Statement> webIDClaimStates = null;
                        
                        switch(this.getDereferenceMode()){
                            case DEREFERENCE_NO:
                                logger.debug("DEREFERENCE_NO getStatements about " + webIDClaim);
                                webIDClaimStates = WebIDUtils.SailUtils.getClaimStatements(webIDClaim, this.readConnection, this.userGraph);
                            break;
                            default :
                                webIDClaimStates = WebIDUtils.HttpUtils.dereference(webIDClaim);
                            break;
                        }
                        //Collection<Statement> webIDClaimStates = WebIDUtils.HttpUtils.dereference(webIDClaim);                        
                        webIDClaimStates.addAll(this.getRoleStatementsAboutURI(webIDClaim));
                        WebIDUser webIDUser = new WebIDUserImpl(new URIImpl(webIDClaim.toString()), webIDClaimStates);
                        
                        logger.debug("WebIDDatabaseImpl checking " + webIDUser);
                        if(WebIDUtils.CertificateUtils.verify(usercert, webIDUser.getRSAPublicKeys())){
                            logger.debug("WebIDDatabaseImpl checking " + webIDUser + " is valid");                            
                            return webIDUser;
                        }
                    } catch (DereferencingException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);                    
                    } catch (SSLHandshakeException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);                    
                    } catch (IOException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);
                    } catch (RDFParseException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);
                    } catch (RDFHandlerException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);
                    } catch (NoRSAPublicKeyException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);
                    } catch (NoSuchSubjectException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);
                    } catch (NoModulusException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);
                    } catch (NoPublicExponentException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);
                    } catch (InvalidKeyException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);
                    } catch (PublicExponentMismatchException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);
                    } catch (ModulusMismatchException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);
                    } catch (TransformerException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);
                    } catch (UnsupportedContentTypeException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);
                    } catch (RepositoryException ex) {
                        this.addException(dummyURI, webIDClaim, ex, webIDStates);
                        logger.warn(ex.getMessage(), ex);
                    } 
                }
            } catch (CertificateParsingException ex) {
                this.addException(dummyURI, null, ex, webIDStates);
                logger.warn(ex.getMessage(), ex);
            } catch (NoWebIDFoundException ex) {
                this.addException(dummyURI, null, ex, webIDStates);
                logger.warn(ex.getMessage(), ex);
            }
        } catch (CertificateExpiredException ex) {
            this.addException(dummyURI, null, ex, webIDStates);
            logger.warn(ex.getMessage(), ex);
        } catch (CertificateNotYetValidException ex) {
            this.addException(dummyURI, null, ex, webIDStates);
            logger.warn(ex.getMessage(), ex);
        }        
        try {
            return new DebugWebIDUserImpl(dummyURI, webIDStates);
        } catch (DereferencingException ex) {
            return null;
        }
    }
    
    @Override
    public MODE getDereferenceMode() {        
        return this.mode==null?WebIDConstants.MODE.DEREFERENCE_ONLY:this.mode;
    }
    
    @Override
    public void setDereferenceMode(WebIDConstants.MODE mode){
        this.mode = mode;
    }
    
    private Collection<Statement> getRoleStatementsAboutURI(URI uri){
        try {
            return this.readConnection.getStatements(new URIImpl(uri.toString()), null, null, true, this.roleGraph).asList();
        } catch (RepositoryException ex) {
            logger.error(ex.getMessage(), ex);
            return new ArrayList<Statement>();
        }
    }
       
    public void addException(org.openrdf.model.URI uri, URI thrower, Throwable ex, Collection<Statement> states){  
        BNode bnode = this.readConnection.getValueFactory().createBNode();
        org.openrdf.model.URI exceptionURI = JAVA.getURI(ex.getClass());
        states.add(new StatementImpl(uri, JAVA.KEYWORD.THROWS, bnode));
        states.add(new StatementImpl(bnode, RDF.TYPE, exceptionURI));                
        if(ex.getMessage()!=null){
            states.add(new StatementImpl(bnode, JAVA.DATA.MESSAGE, this.readConnection.getValueFactory().createLiteral(ex.getMessage())));        
        }
        if(thrower != null){
            try {
                states.add(new StatementImpl(bnode, RDFS.SEEALSO, this.readConnection.getValueFactory().createURI(thrower.toString())));
            } catch(Exception e){
                logger.warn("Cannot add thrower " + thrower);
            }
        }
    }

    @Override
    public void setRoleGraph(String roleGraph) {
        if(roleGraph!=null){
            this.roleGraph = new Resource[]{ new URIImpl(roleGraph) };
        }        
    }

    @Override
    public void setUserGraph(String userGraph) {
        if(userGraph!=null){
            this.userGraph = new Resource[]{ new URIImpl(userGraph) };
        }
    }
    
    public static void main(String[] args){
        String modulus = "bc6dc4bec2a89a3b72cf3174e6a3d9897ebdfce5bb43394d85cf1eade4b1a783a57d922cfc55b629bfe7bd78ad35a8059632d036a2071e21a6bae5e6fb14396609012d8c8ec2e93843775c40e599221c5a37604042a36707febe22e73234a47959c09fd148e7edeb2e68eba2a5731fd91b35e39fdc17e719164887b716fe8f0b40b19bd903cf68706d471825266e93bc378f802fea537e1ed48c0a1f55dde27c7fe8816aeadf3d4c6adf031c656c8e11824d8427cce2ef76d4659467281fa3783b8efe814d407792b69782f430ee86c3cf65b30d6055991105227ccc33aaeb8ea0a9e777f1fac030af76d96ab4224e19b82e7e3a504492d6739b66c10bea4285";
        String exponent = "65537";
        BigInteger m = new BigInteger(modulus, 16);
        BigInteger e = new BigInteger(exponent, 10);
        try {
            RSAPublicKey key = new RSAPublicKeyImpl(m,e);
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
        }
    }
    
}

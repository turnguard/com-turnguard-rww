package com.turnguard.rww.graphstore.rdf4j;

import com.turnguard.rww.graphstore.base.GraphStoreServletBase;
import com.turnguard.rww.graphstore.exception.ContentTypeMismatchException;
import com.turnguard.rww.graphstore.exception.GraphRestrictionException;
import com.turnguard.rww.graphstore.exception.ResourceNotFoundException;
import com.turnguard.rww.graphstore.exception.UnsupportedContentTypeException;
import com.turnguard.rww.graphstore.rdf4j.backend.exception.NoSuchRdf4JBackendException;
import com.turnguard.rww.graphstore.rdf4j.backend.impl.Rdf4JBackendRegistryImpl;
import com.turnguard.rww.graphstore.rdf4j.utils.StatementInserter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.openrdf.model.Resource;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.query.Dataset;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.query.algebra.UpdateExpr;
import org.openrdf.query.parser.ParsedOperation;
import org.openrdf.query.parser.ParsedUpdate;
import org.openrdf.query.parser.QueryParserUtil;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public class Rdf4JGraphStoreServlet extends GraphStoreServletBase {

    private final String INIT_PARAM_BACKEND_IMPL = "backend.impl";
    
    private Repository repository;
    
    @Override
    public void init(ServletConfig config) throws ServletException {        
        super.init(config);        
        if(config.getInitParameter(INIT_PARAM_BACKEND_IMPL)==null){
            throw new ServletException("Missing backend.impl init-parameter");
        }
        try {
            Class clazz = Class.forName(config.getInitParameter(INIT_PARAM_BACKEND_IMPL));
            this.repository = Rdf4JBackendRegistryImpl
                                .getInstance()
                                .createRepository(clazz, config);
        } catch (ClassNotFoundException | NoSuchRdf4JBackendException | RepositoryException ex) {
            throw new ServletException(ex);
        }
        
    }
    
    @Override
    public void destroy() {
        super.destroy();
        try {
            this.repository.shutDown();
        } catch (RepositoryException ex) {}
    }
    
    @Override
    protected boolean graphExists(String graph) {
        RepositoryConnection repCon = null;
        try {
            repCon = this.repository.getConnection();
            return this.graphExists(graph, repCon);
        } catch(QueryEvaluationException | MalformedQueryException | RepositoryException re){
            return false;
        } finally {
            if(repCon!=null){
                try {
                    repCon.close();
                } catch (RepositoryException ex) {}
            }
        }
    }


    @Override
    protected void serializeGraph(String graph, String contentType, PrintWriter writer) throws ResourceNotFoundException {
        graph = graph.equalsIgnoreCase("DEFAULT")?graph:"<"+graph+">";        
        RepositoryConnection repCon = null;
        try {
            repCon = this.repository.getConnection();
            repCon.prepareGraphQuery(
                    QueryLanguage.SPARQL, 
                    "CONSTRUCT { ?s ?p ?o } WHERE { GRAPH <graph> { ?s ?p ?o } }".replaceFirst("<graph>", graph)
                    ).evaluate(Rio.createWriter(RDFFormat.forMIMEType(contentType), writer));            
        } catch(RDFHandlerException | QueryEvaluationException | MalformedQueryException | RepositoryException re){
            
        } finally {
            if(repCon!=null){
                try {
                    repCon.close();
                } catch (RepositoryException ex) {}
            }
        }        
    }

    @Override
    protected void replaceGraph(String graph, String contentType, Reader reader) throws UnsupportedContentTypeException, ContentTypeMismatchException {
        RepositoryConnection repCon = null;
        try {
            repCon = this.repository.getConnection();
            this.deleteGraph(graph, repCon);
            this.addToGraph(graph, contentType, reader, repCon);
        } catch( RepositoryException | IOException | RDFParseException | RDFHandlerException | MalformedQueryException | UpdateExecutionException re){
        } finally {
            if(repCon!=null){
                try {
                    repCon.close();
                } catch (RepositoryException ex) {}
            }
        }        
    }

    @Override
    protected void createGraph(String graph, String contentType, Reader reader) throws UnsupportedContentTypeException, ContentTypeMismatchException {
        RepositoryConnection repCon = null;
        try {
            repCon = this.repository.getConnection();
            this.addToGraph(graph, contentType, reader, repCon);
        } catch( RepositoryException | IOException | RDFParseException | RDFHandlerException re){
        } finally {
            if(repCon!=null){
                try {
                    repCon.close();
                } catch (RepositoryException ex) {}
            }
        }        
    }

    @Override
    protected void deleteGraph(String graph) {
        RepositoryConnection repCon = null;
        try {
            repCon = this.repository.getConnection();
            repCon.begin();
            this.deleteGraph(graph, repCon);
            repCon.commit();
        } catch(UpdateExecutionException | MalformedQueryException | RepositoryException re){
            try {
                if(repCon!=null){
                    repCon.rollback();
                }
            } catch(RepositoryException re1){}
        } finally {
            if(repCon!=null){
                try {
                    repCon.close();
                } catch (RepositoryException ex) {}
            }
        }
    }    

    @Override
    protected void addToGraph(String graph, String contentType, Reader reader) throws UnsupportedContentTypeException, ContentTypeMismatchException {
        RepositoryConnection repCon = null;
        try {
            repCon = this.repository.getConnection();
            repCon.begin();
            this.addToGraph(graph, contentType, reader, repCon);
            repCon.commit();
        } catch(IOException | RDFHandlerException | RDFParseException | RepositoryException re){
            try {
                if(repCon!=null){
                    repCon.rollback();
                }
            } catch(RepositoryException re1){}
        } finally {
            if(repCon!=null){
                try {
                    repCon.close();
                } catch (RepositoryException ex) {}
            }
        }    
    }
    
    @Override
    protected void patchGraph(String graph, String sparqlUpdateQuery) throws GraphRestrictionException {
        RepositoryConnection repCon = null;
        ParsedOperation pO;
        try {
            repCon = this.repository.getConnection();
            repCon.begin();            
            pO = QueryParserUtil.parseOperation(QueryLanguage.SPARQL, sparqlUpdateQuery, graph);
            if(pO instanceof ParsedUpdate){
                for(Map.Entry<UpdateExpr,Dataset> entry : ((ParsedUpdate)pO).getDatasetMapping().entrySet()) {
                    if(!entry.getValue().getDefaultInsertGraph().equals(new URIImpl(graph))){
                        throw new GraphRestrictionException("The update query touches not only the given graph");
                    }
                }
                repCon.prepareUpdate(
                        QueryLanguage.SPARQL, 
                        pO.getSourceString()
                ).execute();
            }
            repCon.commit();
        } catch(UpdateExecutionException | MalformedQueryException | RepositoryException re){
            try {
                if(repCon!=null){
                    repCon.rollback();
                }
            } catch(RepositoryException re1){}
        } finally {
            if(repCon!=null){
                try {
                    repCon.close();
                } catch (RepositoryException ex) {}
            }
        }
    }
    
    
    
    private boolean graphExists(String graph, RepositoryConnection repCon) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
        graph = graph.equalsIgnoreCase("DEFAULT")?graph:"<"+graph+">";
        return repCon.prepareBooleanQuery(
                QueryLanguage.SPARQL, 
                "ASK { GRAPH <graph> { ?s ?p ?o } }".replace("<graph>", graph)
        ).evaluate();    
    }
    
    private void deleteGraph(String graph, RepositoryConnection repCon) throws RepositoryException, MalformedQueryException, UpdateExecutionException{
        graph = graph.equalsIgnoreCase("DEFAULT")?graph:"<"+graph+">";        
        repCon.prepareUpdate(
                QueryLanguage.SPARQL, 
                "DROP SILENT GRAPH <graph>".replaceFirst("<graph>", graph)
        ).execute();    
    }
    
    private void addToGraph(String graph, String contentType, Reader reader, RepositoryConnection repCon) throws IOException, RDFParseException, RDFHandlerException{
        RDFParser parser = Rio.createParser(RDFFormat.forMIMEType(contentType));
                  parser.setRDFHandler(new StatementInserter(repCon, new Resource[]{ new URIImpl(graph) }));
                  parser.parse(reader, graph);
    }
}

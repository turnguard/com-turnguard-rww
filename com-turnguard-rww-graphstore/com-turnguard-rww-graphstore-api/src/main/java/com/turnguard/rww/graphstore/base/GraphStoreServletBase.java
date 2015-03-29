package com.turnguard.rww.graphstore.base;

import com.turnguard.rww.graphstore.GraphStoreRequest;
import com.turnguard.rww.graphstore.GraphStoreResponse;
import com.turnguard.rww.graphstore.GraphStoreServlet;
import com.turnguard.rww.graphstore.exception.ContentTypeMismatchException;
import com.turnguard.rww.graphstore.exception.GraphRestrictionException;
import com.turnguard.rww.graphstore.exception.GraphSyntaxException;
import com.turnguard.rww.graphstore.exception.ResourceNotFoundException;
import com.turnguard.rww.graphstore.exception.UnsupportedContentTypeException;
import com.turnguard.rww.graphstore.impl.GraphStoreRequestImpl;
import com.turnguard.rww.graphstore.impl.GraphStoreResponseImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public abstract class GraphStoreServletBase extends GenericServlet implements GraphStoreServlet {
    
    private String defaultGraph = null;
    private String defaultMimeType = null;
    private List<String> availableMimeTypes = new ArrayList<>();
    private enum HTTP_METHOD {
        GET,
        POST,
        PUT,
        DELETE,
        HEAD,
        PATCH,
        OPTIONS
    }    

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if(config.getInitParameter("default.graph")!=null){
            this.defaultGraph = config.getInitParameter("default.graph");
        }
        if(config.getInitParameter("default.mimetype")!=null){
            this.defaultMimeType = config.getInitParameter("default.mimetype");
        }        
        if(config.getInitParameter("available.mimetypes")!=null){
            this.availableMimeTypes = Arrays.asList(config.getInitParameter("available.mimetypes").split(","));
        }       
    }
    
    @Override
    public void setDefaultGraph(String defaultGraph) {
        this.defaultGraph = defaultGraph;
    }    

    @Override
    public void setDefaultMimeType(String defaultMimeType) {
        this.defaultMimeType = defaultMimeType;
    }

    @Override
    public void setAvailableMimeType(List<String> availableMimeTypes) {
        this.availableMimeTypes = availableMimeTypes;
    }
    
    @Override
    public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        
        GraphStoreRequest gsReq = new GraphStoreRequestImpl(req, this.defaultGraph, this.defaultMimeType, this.availableMimeTypes);
        GraphStoreResponse gsResp = new GraphStoreResponseImpl(resp);
        
        switch(HTTP_METHOD.valueOf(gsReq.getMethod())){            
            case GET:                
                this.doGet(gsReq, gsResp);
            break;            
            case PUT:
                this.doPut(gsReq, gsResp);
            break;
            case DELETE:
                this.doDelete(gsReq, gsResp);
            break;
            case POST:
                this.doPost(gsReq, gsResp);
            break;    
            case HEAD:
                this.doHead(gsReq, gsResp);
            break;    
            case PATCH:
                this.doPatch(gsReq, gsResp);
            break;    
        }
    }
    
    protected void doPatch(GraphStoreRequest gsReq, GraphStoreResponse gsResp) throws IOException {
        //application/sparql-update   
        try {
            if(gsReq.getContentType().equals("application/sparql-update")){
                this.patchGraph(gsReq.getGraph(), IOUtils.toString(gsReq.getReader()));
            }
        } catch (GraphSyntaxException ex) {
            gsResp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The graph parameter MUST be an absolute IRI or default");
        } catch (GraphRestrictionException ex) {
            gsResp.sendError(422, ex.getMessage());
        } 
    }
    
    protected void doHead(GraphStoreRequest gsReq, GraphStoreResponse gsResp) throws IOException{
        try {      
            String x = gsReq.getBestMimeTypeMatch();
            if(this.graphExists(gsReq.getGraph())){
                gsResp.setStatus(HttpServletResponse.SC_OK);
                gsResp.flushBuffer();
            } else {
                gsResp.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested resource was not found");                        
            }
        } catch (GraphSyntaxException ex) {
            gsResp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The graph parameter MUST be an absolute IRI or default");
        } catch (UnsupportedContentTypeException ex) {
            gsResp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "The accepted mimetype cannot be served");
        }    
    }
    
    protected void doGet(GraphStoreRequest gsReq, GraphStoreResponse gsResp) throws IOException{
        try {      
            if(this.graphExists(gsReq.getGraph())){
                this.serializeGraph(gsReq.getGraph(), gsReq.getBestMimeTypeMatch(), gsResp.getWriter());
                gsResp.getWriter().flush();                        
            } else {
                gsResp.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested resource was not found");                        
            }
        } catch (GraphSyntaxException ex) {
            gsResp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The graph parameter MUST be an absolute IRI or default");
        } catch (ResourceNotFoundException ex) {
            gsResp.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested resource was not found");
        } catch (UnsupportedContentTypeException ex) {
            gsResp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "The accepted mimetype cannot be served");
        }    
    }
    
    protected void doPut(GraphStoreRequest gsReq, GraphStoreResponse gsResp) throws IOException{
        try {                    
            if(this.graphExists(gsReq.getGraph())){
                this.replaceGraph(gsReq.getGraph(), gsReq.getContentType(), gsReq.getReader());
                gsResp.setStatus(HttpServletResponse.SC_OK);
            } else {
                this.createGraph(gsReq.getGraph(), gsReq.getContentType(), gsReq.getReader());
                gsResp.setStatus(HttpServletResponse.SC_CREATED);
            }
        } catch (GraphSyntaxException ex) {
            gsResp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The graph parameter MUST be an absolute IRI or default");
        } catch (ContentTypeMismatchException ex) {
            gsResp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ContentType Mismatch");
        } catch (UnsupportedContentTypeException ex) {
            gsResp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported ContentType");
        }        
    }
    
    protected void doDelete(GraphStoreRequest gsReq, GraphStoreResponse gsResp) throws IOException{
        try {
            if(this.graphExists(gsReq.getGraph())){
                this.deleteGraph(gsReq.getGraph());
            } else {
                gsResp.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested resource was not found");
            }
        } catch (GraphSyntaxException ex) {
            gsResp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The graph parameter MUST be an absolute IRI or default");
        }        
    }
    
    protected void doPost(GraphStoreRequest gsReq, GraphStoreResponse gsResp) throws IOException {
        try {                    
            this.addToGraph(gsReq.getGraph(), gsReq.getContentType(), gsReq.getReader());
            gsResp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (GraphSyntaxException ex) {
            gsResp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The graph parameter MUST be an absolute IRI or default");
        } catch (ContentTypeMismatchException ex) {
            gsResp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ContentType Mismatch");
        } catch (UnsupportedContentTypeException ex) {
            gsResp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported ContentType");
        }        
    }    
    
    /* BACKEND SPECIFIC */
    protected abstract boolean graphExists(String graph);
    protected abstract void serializeGraph(String graph, String contentType, PrintWriter writer) throws ResourceNotFoundException;
    protected abstract void replaceGraph(String graph, String contentType, Reader reader) throws UnsupportedContentTypeException, ContentTypeMismatchException;
    protected abstract void createGraph(String graph, String contentType, Reader reader) throws UnsupportedContentTypeException, ContentTypeMismatchException;
    protected abstract void addToGraph(String graph, String contentType, Reader reader) throws UnsupportedContentTypeException, ContentTypeMismatchException;
    protected abstract void deleteGraph(String graph);
    protected abstract void patchGraph(String graph, String sparqlUpdateQuery) throws GraphRestrictionException;
}

package com.turnguard.rww.graphstore.impl;

import com.turnguard.rww.graphstore.GraphStoreConstants;
import com.turnguard.rww.graphstore.GraphStoreRequest;
import com.turnguard.rww.graphstore.exception.GraphSyntaxException;
import com.turnguard.rww.graphstore.exception.UnsupportedContentTypeException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.commonjava.mimeparse.MIMEParse;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public class GraphStoreRequestImpl extends HttpServletRequestWrapper implements GraphStoreRequest {
    
    private final String defaultGraph;
    private final String defaultMimeType;
    private final List<String> availableMimeTypes;
    
    public GraphStoreRequestImpl(ServletRequest request, String defaultGraph, String defaultMimeType, List<String> availableMimeTypes) {
        super((HttpServletRequest)request);
        this.defaultGraph = defaultGraph;
        this.defaultMimeType = defaultMimeType;
        this.availableMimeTypes = availableMimeTypes;
    }

    @Override
    public String getBestMimeTypeMatch() throws UnsupportedContentTypeException {
        String bestMatch = MIMEParse.bestMatch(availableMimeTypes, this.getHeader("accept"));
        if(bestMatch.isEmpty()){
            if(this.defaultMimeType!=null){
                return this.defaultMimeType;
            }
            throw new UnsupportedContentTypeException("");
        }
        return bestMatch;
    }
    
    @Override
    public String getGraph() throws GraphSyntaxException {
        
        if(super.getParameter(GraphStoreConstants.PARAMETERS.DEFAULT)!=null){
            return this.defaultGraph;
        }
        
        if(super.getParameter(GraphStoreConstants.PARAMETERS.GRAPH)!=null){
            try {
                return new URL(super.getParameter(GraphStoreConstants.PARAMETERS.GRAPH)).toURI().toString();
            } catch (MalformedURLException | URISyntaxException ex) {
                throw new GraphSyntaxException(ex);
            }
        }
        
        return super.getRequestURL().toString();
        
    }

    public static void main(String[] args){
        List<String> amt = new ArrayList<>();
        amt.add("text/turtle");
        amt.add("application/rdf+xml");
        System.out.println(MIMEParse.bestMatch(amt, "text/*;q=0.5,*/*;q=0.1"));
        System.out.println(MIMEParse.bestMatch(amt, "text/turtle;application/rdf+xml"));
        System.out.println(MIMEParse.bestMatch(amt, "application/rdf+xml;text/turtle;text/plain"));
        System.out.println("=>"+MIMEParse.bestMatch(amt, "text/plain")+"<=");
        System.out.println("=>"+MIMEParse.bestMatch(amt, "")+"<=");
   }
}

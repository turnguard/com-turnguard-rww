package com.turnguard.rww.graphstore;

import com.turnguard.rww.graphstore.exception.GraphSyntaxException;
import com.turnguard.rww.graphstore.exception.UnsupportedContentTypeException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public interface GraphStoreRequest extends HttpServletRequest {
    public String getGraph() throws GraphSyntaxException;
    public String getBestMimeTypeMatch() throws UnsupportedContentTypeException;
}

package com.turnguard.rww.graphstore.impl;

import com.turnguard.rww.graphstore.GraphStoreResponse;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public class GraphStoreResponseImpl extends HttpServletResponseWrapper implements GraphStoreResponse {

    public GraphStoreResponseImpl(ServletResponse response) {
        super((HttpServletResponse)response);
    }

}

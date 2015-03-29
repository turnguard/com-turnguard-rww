package com.turnguard.rww.graphstore;

import java.util.List;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public interface GraphStoreServlet {
    public void setDefaultGraph(String defaultGraph);
    public void setDefaultMimeType(String defaultMimeType);
    public void setAvailableMimeType(List<String> availableMimeTypes);
}

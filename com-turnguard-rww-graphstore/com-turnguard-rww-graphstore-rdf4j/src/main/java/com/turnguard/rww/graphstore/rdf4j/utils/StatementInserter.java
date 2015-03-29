package com.turnguard.rww.graphstore.rdf4j.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.helpers.RDFHandlerBase;

/**
 *
 * @author http://www.turnguard.com/turnguard
 */
public class StatementInserter extends RDFHandlerBase {

    private final RepositoryConnection repCon;
    private final Resource[] graphs;
    
    public StatementInserter(RepositoryConnection repCon, Resource[] graphs){
        this.repCon = repCon;
        this.graphs = graphs;
    }
    
    @Override
    public void handleStatement(Statement st) throws RDFHandlerException {
        try {
            System.out.println("ADD STATEMENT " + st);
            this.repCon.add(st, graphs);
        } catch (RepositoryException ex) {
            throw new RDFHandlerException(ex);
        }
    }
}

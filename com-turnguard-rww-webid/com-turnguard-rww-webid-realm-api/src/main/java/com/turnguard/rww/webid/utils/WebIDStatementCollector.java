package com.turnguard.rww.webid.utils;

import com.turnguard.rww.webid.vocabulary.CERT;
import com.turnguard.rww.webid.vocabulary.WEBID;
import java.util.Collection;
import java.util.Map;
import org.openrdf.model.Statement;
import org.openrdf.model.vocabulary.FOAF;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.rio.helpers.StatementCollector;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class WebIDStatementCollector extends StatementCollector {

    public WebIDStatementCollector(Collection<Statement> statements, Map<String, String> namespaces) {
        super(statements, namespaces);
    }

    public WebIDStatementCollector(Collection<Statement> statements) {
        super(statements);
    }

    public WebIDStatementCollector() {
    }

    @Override
    public void handleStatement(Statement st) {
        if(!st.getPredicate().equals(WEBID.HAS_ROLE)){ 
            if(st.getPredicate().equals(RDF.TYPE) || st.getPredicate().getNamespace().equals(FOAF.NAMESPACE) || st.getPredicate().getNamespace().equals(CERT.NAMESPACE)){
                super.handleStatement(st);
            }            
        }
    }
        
}

package com.turnguard.rww.webid.utils;

import com.turnguard.rww.webid.exceptions.DereferencingException;
import com.turnguard.rww.webid.exceptions.ExponentDatatypeMismatchException;
import com.turnguard.rww.webid.exceptions.ModulusDatatypeMismatchException;
import com.turnguard.rww.webid.exceptions.ModulusMismatchException;
import com.turnguard.rww.webid.exceptions.NoRSAPublicKeyException;
import com.turnguard.rww.webid.exceptions.NoWebIDFoundException;
import com.turnguard.rww.webid.exceptions.PublicExponentMismatchException;
import com.turnguard.rww.webid.exceptions.UnsupportedContentTypeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class WebIDUtils {

    public static class CertificateUtils {

        private static final char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'a', 'b', 'c', 'd', 'e', 'f'};

        public static Collection<java.net.URI> getWebIDClaims(X509Certificate usercert) throws CertificateParsingException, NoWebIDFoundException {
            Collection<java.net.URI> webIDClaims = new ArrayList<java.net.URI>();
            Collection<List<?>> alternativeNames = usercert.getSubjectAlternativeNames();
            if (alternativeNames != null) {
                for (List<?> alternativeName : alternativeNames) {
                    Integer id = (Integer) alternativeName.get(0);
                    if (id == 6) {
                        try {
                            webIDClaims.add(new java.net.URI((String) alternativeName.get(1)));
                        } catch (Exception e) {
                        }
                    }
                }
            }
            if(webIDClaims.isEmpty()){
                throw new NoWebIDFoundException("Didn't find any webIDClaim");
            }
            return webIDClaims;
        }

        public static boolean verify(X509Certificate usercert, Collection<RSAPublicKey> rsaPublicKeys) throws PublicExponentMismatchException, ModulusMismatchException, NoRSAPublicKeyException{
            RSAPublicKey usercertRSAPublicKey = (RSAPublicKey) usercert.getPublicKey();

            boolean foundMatchingPublicExponent = false;
            boolean foundMatchingModulus = false;

            for(RSAPublicKey rsaPublicKey : rsaPublicKeys){

                boolean foundCorrectPublicExponent = false;
                boolean foundCorrectModulus = false;

                if(rsaPublicKey.getModulus().equals(usercertRSAPublicKey.getModulus())){
                    foundMatchingModulus = true;
                    foundCorrectModulus = true;
                }
                if(rsaPublicKey.getPublicExponent().equals(usercertRSAPublicKey.getPublicExponent())){
                    foundMatchingPublicExponent = true;
                    foundCorrectPublicExponent = true;
                }
                if(foundCorrectPublicExponent && foundCorrectModulus){
                    return true;
                }
            }
            if(foundMatchingPublicExponent && foundMatchingModulus){
                throw new PublicExponentMismatchException("Found matching modulus, but public exponents in WebID and certificate mismatch");
            }
            if(foundMatchingPublicExponent && !foundMatchingModulus){
                throw new ModulusMismatchException("Found matching public exponent, but modulus in WebID and certificate mismatch");
            }
            if(!foundMatchingPublicExponent && foundMatchingModulus){
                throw new PublicExponentMismatchException("Found matching modulus, but public exponents in WebID and certificate mismatch");
            }
            if(!foundMatchingPublicExponent && !foundMatchingModulus){
                throw new NoRSAPublicKeyException("Didn't find any matching public exponent or modulus");
            }

            return false;
        }

        public static BigInteger getModulus(HashSet<Value> moduli) throws ModulusDatatypeMismatchException{
            for(Value modulus : moduli){
                Literal mod = (Literal)modulus;
                if(mod.getDatatype()==null || !mod.getDatatype().equals(XMLSchema.HEXBINARY)){
                    throw new ModulusDatatypeMismatchException("Found cert#modulus in WebIDProfileDocument that is not of datatype " + XMLSchema.HEXBINARY);
                }
                BigInteger m = new BigInteger(cleanHexString(modulus.stringValue()), 16);
                return m;
            }
            return null;
        }

        public static BigInteger getExponent(HashSet<Value> exponents) throws ExponentDatatypeMismatchException{
            for(Value exponent : exponents){
                Literal exp = (Literal)exponent;
                if(exp.getDatatype()==null || (!exp.getDatatype().equals(XMLSchema.INTEGER) && !exp.getDatatype().equals(XMLSchema.INT))){
                    throw new ExponentDatatypeMismatchException("Found cert#exponent in WebIDProfileDocument that is not of datatype " + XMLSchema.INTEGER + " or " + XMLSchema.INT);
                }
                BigInteger e = new BigInteger(exponent.stringValue(), 10);
                return e;
            }
            return null;
        }

        private static String cleanHexString(String val) {
            StringBuilder buffer = new StringBuilder();
            for (char c : val.toCharArray()) {
                if (Arrays.binarySearch(hexChars, c) >= 0) {
                    buffer.append(c);
                }
            }
            return buffer.toString();
        }
    }

    public static class HttpUtils {

        private final static Logger logger = Logger.getLogger(HttpUtils.class);

        public static Collection<Statement> dereference(java.net.URI uri) throws IOException, RDFParseException, RDFHandlerException, TransformerException, UnsupportedContentTypeException, SSLHandshakeException, DereferencingException  {
            logger.debug("dereferencing " + uri.toString());
            StatementCollector collector = new WebIDStatementCollector();
            HttpURLConnection connection = null;
            InputStream in = null;
            try {
                if(uri.getScheme().equals("https")){
                    connection = (HttpsURLConnection) uri.toURL().openConnection();
                } else {
                    connection = (HttpURLConnection) uri.toURL().openConnection();
                }
                connection.setInstanceFollowRedirects(true);
                connection.addRequestProperty("Accept", getAcceptHeader());
                if(uri.toString().indexOf("#")==-1 && connection.getResponseCode()==200 && connection.getURL().equals(uri.toURL())){
                    throw new com.turnguard.rww.webid.exceptions.DereferencingException(uri + " is not a URI but a URL");
                }
                in = connection.getInputStream();

                RDFFormat foafFormat = null;
                String contentType = connection.getContentType();
                logger.debug("dereferencing " + uri.toString() + " retrieved contentType " + contentType);
                String mimeType = null;
                if (contentType.indexOf(";") != -1) {
                    mimeType = contentType.split(";")[0];
                } else {
                    mimeType = contentType;
                }
                if (mimeType.equals("text/turtle")) {
                    mimeType = "application/x-turtle";
                }
                foafFormat = RDFFormat.forMIMEType(mimeType);
                if(foafFormat==null){
                    throw new UnsupportedContentTypeException("Content-Type : " + mimeType +" is not supported");
                }
                RDFParser parser = Rio.createParser(foafFormat);
                parser.setRDFHandler(collector);
                parser.setPreserveBNodeIDs(true);

                parser.parse(in, base(uri.toString()));
            } finally {
                if (in != null) {
                    in.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            logger.debug("Returning " + collector.getStatements().size() + " statements for " + uri);
            return collector.getStatements();
        }

        private static String getAcceptHeader() {
            StringBuilder formatHeader = new StringBuilder();
            for (RDFFormat format : RDFFormat.values()) {
                formatHeader.append(";");
                formatHeader.append(format.getDefaultMIMEType());
            }
            formatHeader.append(";");
            formatHeader.append("text/turtle");
            System.out.println("header=>"+formatHeader.toString().replaceFirst(";", ""));
            return formatHeader.toString().replaceFirst(";", "");
        }

        private static String base(String uri) {
            if (!uri.startsWith("jar:")) {
                return uri;
            }
            int start = uri.indexOf(':') + 1;
            int end = uri.lastIndexOf('!');
            String jar = uri.substring(start, end);
            try {
                String encoded = URLEncoder.encode(jar, "UTF-8");
                return "injar://" + encoded + uri.substring(end + 1);
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError(e);
            }
        }
    }

    public static class SailUtils {
        private static String userQuery = "CONSTRUCT { <WEBIDCLAIM> ?p ?o . ?key ?p ?o } FROM WHERE { { <WEBIDCLAIM> ?p ?o . } UNION { <WEBIDCLAIM> <http://www.w3.org/ns/auth/cert#key> ?key . ?key ?p ?o } }";
        public static HashMap<Resource,HashMap<URI,HashSet<Value>>> toHashMap(Collection<Statement> states){
            HashMap<Resource,HashMap<URI,HashSet<Value>>> data = new HashMap<Resource,HashMap<URI,HashSet<Value>>>();
            for(Statement state : states){
                Resource subject = state.getSubject();
                URI predicate = state.getPredicate();
                Value object = state.getObject();
                if(!data.containsKey(subject)){
                    HashMap<URI, HashSet<Value>> predicates = new HashMap<URI,HashSet<Value>>();
                    HashSet<Value> objects = new HashSet<Value>();
                                   objects.add(object);
                                   predicates.put(predicate, objects);
                                   data.put(subject, predicates);
                } else {
                    if(!data.get(subject).containsKey(predicate)){
                        HashSet<Value> objects = new HashSet<Value>();
                                       objects.add(object);
                                       data.get(subject).put(predicate, objects);
                    } else {
                        data.get(subject).get(predicate).add(object);
                    }
                }
            }
            return data;
        }

        public static Collection<Statement> getClaimStatements(java.net.URI webIDClaim, RepositoryConnection repCon, Resource[] userGraph) throws RepositoryException{
            StatementCollector collector = new WebIDStatementCollector();
            String claimQuery = SailUtils.userQuery.replaceAll("WEBIDCLAIM", webIDClaim.toString());
            if(userGraph!=null && userGraph.length>0){
                StringBuilder from = new StringBuilder();
                for(Resource graph : userGraph){
                    from.append("FROM <");
                    from.append(graph.stringValue());
                    from.append(">\n");
                }
                claimQuery = claimQuery.replaceFirst("FROM", from.toString());
            } else {
                claimQuery = claimQuery.replaceFirst("FROM", "");
            }

            try {
                System.out.println(claimQuery);
                repCon.prepareGraphQuery(QueryLanguage.SPARQL, claimQuery).evaluate(collector);
            } catch (Exception ex) {

            }

            return collector.getStatements();
        }
    }
}

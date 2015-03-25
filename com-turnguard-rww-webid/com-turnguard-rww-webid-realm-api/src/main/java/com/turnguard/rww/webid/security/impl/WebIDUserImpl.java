package com.turnguard.rww.webid.security.impl;

import com.turnguard.rww.webid.exceptions.DereferencingException;
import com.turnguard.rww.webid.exceptions.NoModulusException;
import com.turnguard.rww.webid.exceptions.NoPublicExponentException;
import com.turnguard.rww.webid.exceptions.NoRSAPublicKeyException;
import com.turnguard.rww.webid.exceptions.NoSuchPredicateException;
import com.turnguard.rww.webid.exceptions.NoSuchSubjectException;
import com.turnguard.rww.webid.security.WebIDUser;
import com.turnguard.rww.webid.security.base.WebIDPrincipalBase;
import com.turnguard.rww.webid.utils.WebIDUtils;
import com.turnguard.rww.webid.vocabulary.CERT;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.rio.RDFParseException;
import sun.security.rsa.RSAPublicKeyImpl;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public class WebIDUserImpl extends WebIDPrincipalBase implements WebIDUser {

    public WebIDUserImpl(Resource uri) {
        super(uri);        
    }

    public WebIDUserImpl(Resource uri, Collection<Statement> states) throws DereferencingException {
        super(uri, states);
        if(!this.principalData.containsKey(uri)){
            throw new DereferencingException("No statements about WebIDClaim " + uri + " found.");
        }
    }

    @Override
    public Collection<RSAPublicKey> getRSAPublicKeys() throws NoRSAPublicKeyException, NoSuchSubjectException, NoModulusException, NoPublicExponentException, InvalidKeyException, DereferencingException, RDFParseException {
        if(this.principalData.containsKey(uri)){
            Collection<RSAPublicKey> rsaPublicKeys = new ArrayList<RSAPublicKey>();
            if(this.principalData.get(uri).containsKey(CERT.KEY)){
                for(Value key : this.principalData.get(uri).get(CERT.KEY)){
                    if(key instanceof Resource){
                        HashSet<Value> moduli;
                        try {
                            moduli = this.get((Resource)key, CERT.MODULUS);                            
                        } catch (NoSuchPredicateException ex) {
                            throw new NoModulusException("Cant' find " + CERT.MODULUS.stringValue() + " in " + key.stringValue());
                        }
                        HashSet<Value> exponents;
                        try {
                            exponents = this.get((Resource)key, CERT.EXPONENT);
                        } catch (NoSuchPredicateException ex) {
                            throw new NoPublicExponentException("Cant' find " + CERT.EXPONENT.stringValue() + " in " + key.stringValue());
                        }

                        rsaPublicKeys.add(new RSAPublicKeyImpl(WebIDUtils.CertificateUtils.getModulus(moduli), WebIDUtils.CertificateUtils.getExponent(exponents)));                    
                    }            
                }
            } else {
                throw new NoRSAPublicKeyException("Can't find any " + CERT.KEY.stringValue() + " in the WebID");
            }
            return rsaPublicKeys;
        } else {
            throw new DereferencingException("No statements about WebIDClaim " + uri + " found.");
        }
    }
    
}

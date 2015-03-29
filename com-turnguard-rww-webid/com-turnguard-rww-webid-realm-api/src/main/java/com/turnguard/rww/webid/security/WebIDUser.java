package com.turnguard.rww.webid.security;

import com.turnguard.rww.webid.exceptions.DereferencingException;
import com.turnguard.rww.webid.exceptions.ExponentDatatypeMismatchException;
import com.turnguard.rww.webid.exceptions.ModulusDatatypeMismatchException;
import com.turnguard.rww.webid.exceptions.NoModulusException;
import com.turnguard.rww.webid.exceptions.NoPublicExponentException;
import com.turnguard.rww.webid.exceptions.NoRSAPublicKeyException;
import com.turnguard.rww.webid.exceptions.NoSuchSubjectException;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import org.openrdf.rio.RDFParseException;

/**
 *
 * @author {@link <a href="http://www.turnguard.com/turnguard" target="_blank">turnguard</a>}
 */
public interface WebIDUser extends WebIDPrincipal {
    public Collection<RSAPublicKey> getRSAPublicKeys() throws ModulusDatatypeMismatchException, ExponentDatatypeMismatchException, DereferencingException, NoRSAPublicKeyException, NoSuchSubjectException, NoModulusException, NoPublicExponentException, InvalidKeyException, RDFParseException;    
}

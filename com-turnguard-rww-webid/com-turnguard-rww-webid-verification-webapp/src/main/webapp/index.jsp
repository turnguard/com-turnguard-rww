<%@page import="org.openrdf.model.Value"%>
<%@page import="org.openrdf.model.Resource"%>
<%@page import="com.turnguard.rww.webid.vocabulary.CERT"%>
<%@page import="com.turnguard.rww.webid.exceptions.NoSuchPredicateException"%>
<%@page import="com.turnguard.rww.webid.vocabulary.FOAF"%>
<%@page import="com.turnguard.rww.webid.security.WebIDPrincipal"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>WebID Verification WebApp</title>
    </head>
    <body>
        <h1>Successfully logged in!</h1>
        <%
            WebIDPrincipal principal = (WebIDPrincipal)request.getUserPrincipal();
        %>
        <h2>URI => <%=principal.getURI()%></h2><br/>
        
        foaf:givenName => <% try { out.print(principal.get(FOAF.GIVENNAME)); } catch(NoSuchPredicateException nspe){} %><br/>
        foaf:familyName => <% try { out.print(principal.get(FOAF.FAMILYNAME)); } catch(NoSuchPredicateException nspe){}%><br/>
        foaf:firstName => <% try { out.print(principal.get(FOAF.FIRSTNAME)); } catch(NoSuchPredicateException nspe){}%><br/>
        foaf:lastName => <% try { out.print(principal.get(FOAF.LASTNAME)); } catch(NoSuchPredicateException nspe){}%><br/>
        foaf:nick => <% try { out.print(principal.get(FOAF.NICK)); } catch(NoSuchPredicateException nspe){}%><br/>
        foaf:mbox => <% try { out.print(principal.get(FOAF.MBOX)); } catch(NoSuchPredicateException nspe){}%><br/>
        foaf:knows => <% try { out.print(principal.get(FOAF.KNOWS)); } catch(NoSuchPredicateException nspe){}%><br/>        
        
        cert:key => <% 
            try { 
                out.print(principal.get(CERT.KEY)+"<br/>");
                for(Value r : principal.get(CERT.KEY)){
                    out.print("cert:exponent => "+principal.get((Resource)r, CERT.EXPONENT)+"<br/>");
                    out.print("cert:modulus => "+principal.get((Resource)r, CERT.MODULUS)+"<br/><br/>");
                }                
            } catch(NoSuchPredicateException nspe){}
        %><br/>
    </body>
</html>

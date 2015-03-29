<%@page import="com.turnguard.rww.webid.vocabulary.JAVA"%>
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
        
        java:throws => <% 
            try { 
                out.print(principal.get(JAVA.KEYWORD.THROWS)+"<br/>");
                for(Value r : principal.get(JAVA.KEYWORD.THROWS)){
                    out.print("java:ex => "+principal.get((Resource)r, JAVA.DATA.MESSAGE)+"<br/>");                    
                }                
            } catch(NoSuchPredicateException nspe){}
        %><br/>
    </body>
</html>

<%-- 
    Document   : config
    Created on : 22-04-2012, 19:14:13
    Author     : LocNguyen
--%>

<%@page import="java.util.StringTokenizer"%>
<%@page import="org.omg.PortableInterceptor.SYSTEM_EXCEPTION"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="uit.cnpm02.dkhp.utilities.BOUtils"%>
<%@page import="java.util.Properties"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%
    // Load system configuration
    Properties prop = BOUtils.getConfig();
    Map<String, String> propMap = new HashMap<String, String>((Map) prop);

    Set<Map.Entry<String, String>> propSet;
    propSet = propMap.entrySet();

    // System properties
    
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>System configuration</title>
    </head>
    <body>
        <b>System information</b> <br />
        <table>
            <tr>
                <td>IP: </td>
                    <td><%= request.getRemoteAddr() %></td>
                </tr>
                <tr>
                    <td>Current user: </td>
                    <td><%= request.getRemoteUser() %></td>
                </tr>
                <% 
                  // get client locale
                  java.util.Locale locale = request.getLocale();

                  // get Dateformat for client's locale
                  java.text.DateFormat dateFormat =
                         java.text.DateFormat.getDateTimeInstance(
                                 java.text.DateFormat.LONG,
                                 java.text.DateFormat.LONG, locale);

                %>
                <tr>
                    <td>Time</td>
                    <td><%=dateFormat.format( new java.util.Date() ) %></td>
                </tr>
            </table>
                
        <b>System configuraiton</b> <br />
        <table>
            <%
            boolean isOodRow = false;
            for (Map.Entry<String, String> me : propSet) {
                String rowCSS = (isOodRow ? "odd-row" : "even-row");
                isOodRow = !isOodRow;
            %>
            <tr class="<%= rowCSS %>" >
                <td><%= me.getKey() %></td>
                <td> <input type="text" value="<%= me.getValue() %>" /> </td>
            </tr>
            <%}%>
            
        </table>
        
        <input type="button" value="Save" onclick="saveConfig()" />
            
            
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script src="../../javascripts/jquery-1.7.1.js"></script>
    <SCRIPT language="javascript">
        var http = createRequestObject();
        
        function saveConfig() {
            alert("Tobe implemented...");
        }
        
    </script>
    </body>
    
</html>

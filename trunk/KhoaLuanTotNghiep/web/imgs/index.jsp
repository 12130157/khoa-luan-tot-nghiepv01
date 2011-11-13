<%-- 
    Document   : index
    Created on : 27-04-2011, 22:06:10
    Author     : ngloc_it
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link href="../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style media="all" type="text/css">
            a{
                height: 120px;
                margin-left: 45%;
                margin-top: 350px;
                background-color: #ff478d;
                border: 2px solid #2f4e3d;
                text-transform: uppercase;
            }
            a:hover{
                background-color: #d2ff80;
            }
        </style>
    </head>
    <body>        
        <%
            response.sendRedirect("./jsps/jspTrangChu.jsp");
            //response.sendRedirect("./jsps/jspTest.jsp");
        %>

    </body>
</html>

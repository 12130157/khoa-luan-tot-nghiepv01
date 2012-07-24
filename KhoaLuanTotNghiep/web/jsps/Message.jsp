<%-- 
    Document   : jspTrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link href="../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang Chủ</title>
        <style>
            #msg-range {
                font-weight: bold;
                text-align: center;
                border: 1px solid #DD2;
                height: 100px;
                border-radius: 10px;
                width: 98%;
                background: #DFF;
            }
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%-- Menu --%>
            <%@include file="Menu.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="msg-range">
                    Vui lòng đăng nhập đăng nhập để xem trang này.
                </div>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
</html>
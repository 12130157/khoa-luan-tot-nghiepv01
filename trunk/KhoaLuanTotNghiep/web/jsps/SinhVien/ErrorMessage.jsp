<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@page import="java.util.List"%>
<%@include file="MenuSV.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
<%
List<String> message=(List<String>) session.getAttribute("message");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thông báo</title>
        
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">            
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <br>
                <%if(message.isEmpty()){%>
                <p>Thao tác thành công, <a href="../../RegistryController?action=view">bấm vào đây</a> để quay về trang đăng ký môn học.</p>
                <%}else{
                  for(int i=0;i<message.size();i++){%>
                  <%=message.get(i)%><br>
                <%}%>
                <p>Các lớp học khác đăng ký thành công</p>
                <p><a href="../../RegistryController?action=view">Bấm vào đây</a> để xem kết quả.</p>
                <%}%>
                  
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    </script>
  </html>
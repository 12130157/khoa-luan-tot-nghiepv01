<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@include file="MenuSV.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
/*    List<News> listNews = (List<News>) session.getAttribute("news");

    int newsPerPage = 5;
    int currentPage = 1;
 */ 
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang sinh viên</title>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">            
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <%@include file="News.jsp" %>
           <%--
                <%
                    if (listNews != null) {
                        for (int i = 0; i < listNews.size(); i++) {
                            News n = listNews.get(i);
                            if (listNews.get(i).getType() == 1) {
                %>
                <a href="../../NewsController?Actor=Student&action=detail&Id=<%=n.getId()%>"><%=n.getTitle()%></a><br>
                <%=(n.getContent().length() >= 200 ? n.getContent().substring(0, 200)+"..." : n.getContent())%><br>
                <%}
                            }
                        }
                    %>
           --%>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <script src="../javascripts/News.js"></script>
    <script  type = "text/javascript" >
        action="search";
        var http = createRequestObject();
        function search(){
            name=document.getElementById("subname").value;
            ajaxfunction("../servSubject?action="+action+"&name="+name);
        }
       
    </script>
</html>
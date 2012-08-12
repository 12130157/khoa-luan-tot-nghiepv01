<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.utilities.StringUtils"%>
<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.News"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    // Validate access role
    ClientValidate.validateAcess(AccountType.LECTUTER, session, response);
    
    News newsDetail = (News) session.getAttribute("newsdetail");

%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang Chủ</title>
        <style media="all" type="text/css">
            /**CSS OVERIDDEN**/
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%@include file="MenuGV.jsp"%>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content">
                <%
                    if (newsDetail != null) {
                        %>
                        <div class="news-title"><%=newsDetail.getTitle()%></div>
                        <div class="clear"></div>
                        <div class="news-content">
                            <%=newsDetail.getContent()%>
                        </div>
                        <% if (!StringUtils.isEmpty(newsDetail.getFile())) { %>
                        <div class="file-download"><a href="../../DownloadController?action=download-news-attached-file&filename=<%= newsDetail.getFile() %>"><%= newsDetail.getFile() %></a></div>
                        <%}%>
                   <% }
                %>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->

    <script src="../javascripts/News.js"></script>
    <script  type = "text/javascript" >
        action="search";
        var http = createRequestObject();
        function search(){
            name=document.getElementById("subname").value;
            ajaxfunction("../servSubject?action="+action+"&name="+name);
        }
       
    </script>
    </body>
</html>
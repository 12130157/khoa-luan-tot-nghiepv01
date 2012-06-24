<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="uit.cnpm02.dkhp.model.Task"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.News"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    List<Task> tasks = (List<Task>) session.getAttribute("tasks");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang giảng viên</title>
        <style media="all" type="text/css">
            /**CSS OVERRIDEN**/
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%@include file="MenuGV.jsp"%>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="important-task">
                    <%
                        if ((tasks != null) && !tasks.isEmpty()) {
                    %>
                    <u><b>Tin quan trọng:</b></u>
                    <table class="general-table important-table">
                        <tr>
                            <th> STT </th>
                            <th> Nội dung </th>
                            <th> Người gửi </th>
                            <th> Ngày gửi </th>
                        </tr>
                        <%
                            for (int i = 0; i < tasks.size(); i++) {
                                Task t = tasks.get(i);
                                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_PARTERM_DEFAULT);
                        %>
                        <tr>
                            <td> <%= (i + 1)%> </td>
                            <td>
                                <%= t.getContent()%>
                                <div class="pop-up">
                                    <span class="atag" onclick="hideTask(<%= t.getId()%>)"> Ẩn </span>
                                </div>
                            </td>
                            <td> <%= t.getSender()%> </td>
                            <td> <%= sdf.format(t.getCreated())%> </td>
                        </tr>
                        <%
                            }
                        %>
                    </table>
                    <div class="clear"></div>
                    <%
                        }
                    %>
                </div>
                <%@include file="News.jsp" %>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <script src="../../javascripts/AjaxUtil.js"></script>
    <script src="../../javascripts/TaskUtil.js"></script>

    <script  type = "text/javascript" >
       
    </script>
</html>
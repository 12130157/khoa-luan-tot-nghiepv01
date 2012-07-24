<%-- 
    Document   : News
    Created on : 01-05-2012, 07:25:00
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.utilities.Constants"%>
<%@page import="uit.cnpm02.dkhp.model.News"%>
<%@page import="java.util.List"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%
    // Validate access role
    ClientValidate.validateAcess(AccountType.LECTUTER, session, response);
    
    List<News> listNews = (List<News>) session.getAttribute("news");

    int newsPerPage = 5;
    int currentPage = 1;
%>
<html>
    <head>
        <link href="../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            if (listNews != null) {
                for (int i = 0; i < listNews.size(); i++) {
                    News n = listNews.get(i);
                    if (listNews.get(i).getType() == Constants.NEWS_TYPE_UNREAD) {
        %>
        <div id="new-range">
            <div id="image">
                <%
                    if ((n.getImgPath() != null) && !n.getImgPath().isEmpty()) {
                %>
                <img src="<%= "../" + n.getImgPath()%>" alt="<%= n.getTitle()%>" />
                <%
                    }
                %>
            </div>
            <div id="title">
                <a href="../NewsController?Actor=normal&action=detail&Id=<%=n.getId()%>"><%=n.getTitle()%></a><br>
            </div>
            <div id="short-description">
                <%=(n.getContent().length() >= 200 ? n.getContent().substring(0, 200) + "..." : n.getContent())%><br>
            </div>
            <div id="new-to-detail">
                <a href="../NewsController?Actor=normal&action=detail&Id=<%=n.getId()%>"> Chi tiết </a>
            </div>
        </div>
        <div class="clear"></div>
        <%}
                }
            }
        %>
    </body>
</html>

<%-- 
    Document   : ListStudent
    Created on : 16-11-2011, 22:06:24
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.Diary"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<Diary> diaries = (List<Diary>) session.getAttribute("diaries");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Long management</title>
        <style media="all" type="text/css">
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%@include file="MenuPDT.jsp"%>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Nhật ký sử dụng web site
                </div>
                <br /><br />
                
                <div id="">
                    <table class="general-table">
                        <tr>
                            <th>STT</th>
                            <th>User</th>
                            <th>Nội dung</th>
                            <th>Thời gian</th>
                        </tr>
                        <%
                            if (diaries != null && !diaries.isEmpty()) {
                                for (int i = 0; i < diaries.size(); i++) {
                                    Diary d = diaries.get(i);%>
                                <tr>
                                    <td> <%= (i + 1) %> </td>
                                    <td> <span class="atag" onclick="loadUserDetail('<%=d.getUserName() %>')"><%= d.getUserName() %></span> </td>
                                    <td> <span class="atag" onclick="loadContent(<%=d.getId()%>)"><%= d.getContent() %></span></td>
                                    <td> <%= d.getDate() %> </td>
                                </tr>
                            <%}}%>
                    </table>
                </div>
                <%-- User detail popup window --%>
                <div id="popup-detail" class="popup-hidden" onClick="hideMe('popup-detail');">
                    popup window...
                </div>
                <%--  PAGGING --%>
                <div id="paggind">
                    <input type="button" value="|<<" onclick="firstPage()" />
                    <input type="button" value="<<" onclick="prePage()" />
                    <input type="button" value=">>" onclick="nextPage()" />
                    <input type="button" value=">>|" onclick="endPage()" /><br />
                    <input type="hidden" value="<%= 1%>" id="numpage" />
                </div>
                <h1></h1>
            </div><!--End Contents-->
            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    
        <script src="../../javascripts/AjaxUtil.js"></script>
        <script type = "text/javascript">
            var http = createRequestObject();

            function loadUserDetail(username) {
                initPopupWindowAtMousePossition('popup-detail');
                //setTimeOut('popup-detail', 10);
                $('#popup-detail').fadeIn('slow', function() {
                // Animation complete
                    var controller = '../../DiaryController?action=get-user-detail' 
                                + '&user=' + username;
                    if(http){
                        http.open("GET", controller, true);
                        http.onreadystatechange = getUserDetailHandler;
                        http.send(null);
                    } else {
                        alert("Lỗi gửi yêu cầu tới Server thất bại");
                    }
                });
            }
            
            function getUserDetailHandler() {
                if(http.readyState == 4 && http.status == 200){
                     var detail = document.getElementById("popup-detail");
                     detail.innerHTML = http.responseText;
                }    
            }
            
            // Diary id
            function loadContent(id) {
                initPopupWindowAtMousePossition('popup-detail');
                $('#popup-detail').fadeIn('slow', function() {
                // Animation complete
                    var controller = '../../DiaryController?action=get-content-detail' 
                                + '&id=' + id;
                    if(http){
                        http.open("GET", controller, true);
                        http.onreadystatechange = getUserDetailHandler;
                        http.send(null);
                    } else {
                        alert("Lỗi gửi yêu cầu tới Server thất bại");
                    }
                });
            }
            
            function firstPage() {
                alert("Tobe implemented...");
            }
            
            function endPage() {
                alert("Tobe implemented...");
            }
            
            function nextPage() {
                alert("Tobe implemented...");
            }
            
            function prePage() {
                alert("Tobe implemented...");
            }
        </script>
    </body>
</html>
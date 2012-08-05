<%-- 
    Document   : ListStudent
    Created on : 16-11-2011, 22:06:24
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.controllers.PDT.DiaryController"%>
<%@page import="java.util.Date"%>
<%@page import="uit.cnpm02.dkhp.utilities.DateTimeUtil"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.Diary"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);

    List<Diary> diaries = (List<Diary>) session.getAttribute("diaries");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="../../javascripts/DateTimePicker.js" type="text/javascript"></script>
        <title>Quản lý thay đổi</title>
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
                    <%--------------Search-------------%>
                    <%-- Search normal --%>
                    <div id="search-area" style="float: left;">
                        <%--input form--%>
                        <div id="searchbox" style="float: left;">
                            <input id="search" type="text" onKeyPress="searchBoxKeyPressed(event)" placeholder="Search" />
                            <input type="button" id="submit" onclick="searchLog()" value="Tìm kiếm" />
                        </div>
                        <%-- Search by date --%>
                        <div id="searchbox" style="float: left; width: 560; margin-left: 30px;">
                            <div style="float: left;">
                                Từ <input type="text" id="from-date" style="max-width: 80px;" readonly="readonly" value="<%=DateTimeUtil.format(new Date())%>" />
                                <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('from-date','YYMMMDD')" />
                                Đến <input type="text" id="to-date" style="max-width: 80px;" readonly="readonly" value="<%=DateTimeUtil.format(new Date())%>" />
                                <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('to-date','YYMMMDD')" />
                            </div>
                            <div style="float: left;">
                                <input type="button" id="submit" onclick="searchLogByDate()" value="Tìm kiếm" />
                            </div>
                        </div>
                    </div>
                    <div class="clear"></div>
                    <table class="general-table" id="tbl-list-log">
                        <tr>
                            <th>STT</th>
                            <th>User</th>
                            <th>Nội dung</th>
                            <th>Thời gian</th>
                        </tr>
                        <%
                            if (diaries != null && !diaries.isEmpty()) {
                                for (int i = 0; i < diaries.size(); i++) {
                                    Diary d = diaries.get(i);
                                    String content = d.getContent();
                                    if (content.length() > DiaryController.SHORT_CONTENT_LENGHT) {
                                        content = content.substring(0, DiaryController.SHORT_CONTENT_LENGHT) + "...";
                                    }
                                %>
                                <tr>
                                    <td> <%= (i + 1) %> </td>
                                    <td> <span class="atag" onclick="loadUserDetail('<%=d.getUserName() %>')"><%= d.getUserName() %></span> </td>
                                    <td> <span class="atag" onclick="loadContent(<%=d.getId()%>)"><%= content %></span></td>
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
                <%--
                <div id="paggind">
                    <input type="button" value="|<<" onclick="firstPage()" />
                    <input type="button" value="<<" onclick="prePage()" />
                    <input type="button" value=">>" onclick="nextPage()" />
                    <input type="button" value=">>|" onclick="endPage()" /><br />
                    <input type="hidden" value="<%= 1%>" id="numpage" />
                </div>
                --%>
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
            
            function searchBoxKeyPressed(event) {
                if (event.charCode == '13') {
                    searchLog();
                }
            }
            
            function searchLog() {
                var key = $("#search").val();
                if (key == '') {
                    return;
                }
                
                var controller = '../../DiaryController?action=search-log' 
                                    + '&key=' + key;
                if(http){
                    http.open("GET", controller, true);
                    http.onreadystatechange = searchLogHandler;
                    http.send(null);
                } else {
                    alert("Lỗi gửi yêu cầu tới Server thất bại");
                }
            }
            
            function searchLogHandler() {
                if(http.readyState == 4 && http.status == 200){
                     var detail = document.getElementById("tbl-list-log");
                     if (http.responseText != '') {
                         detail.innerHTML = http.responseText;
                         formatGeneralTable();
                     }
                }
            }
            
            function searchLogByDate() {
                var fromDate = $("#from-date").val();
                var toDate = $("#to-date").val();
                
                var controller = '../../DiaryController?action=search-log-by-date' 
                                    + '&fromdate=' + fromDate
                                    + '&todate=' + toDate;
                if(http){
                    http.open("GET", controller, true);
                    http.onreadystatechange = searchLogByDateHandler;
                    http.send(null);
                } else {
                    alert("Lỗi gửi yêu cầu tới Server thất bại");
                }
            }
            
            function searchLogByDateHandler() {
                if(http.readyState == 4 && http.status == 200){
                     var detail = document.getElementById("tbl-list-log");
                     if (http.responseText != '') {
                         detail.innerHTML = http.responseText;
                         formatGeneralTable();
                     }
                }
            }
        </script>
    </body>
</html>
<%-- 
    Document   : jspmenu
    Created on : Apr 23, 2011, 3:50:32 PM
    Author     : ngloc_it
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link href="../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../csss/general.css" rel="stylesheet" type="text/css" media="screen">
    </head>
    <body>
    <MARQUEE onmouseover="this.stop();" onmouseout="this.start();" HEIGHT=25 BGCOLOR=transparent BEHAVIOR=scroll SCROLLAMOUNT="4">
        <h3>Đại Học Quốc Gia Tp. Hồ Chí Minh - Trường Đại Học Công Nghệ Thông Tin.</h3>
    </MARQUEE>

    <div id="logo">
        <DIV id="flash" style="Z-INDEX: 1; padding-left: 10%; padding-top: 0px; WIDTH: 80%; POSITION: absolute; TOP: 0px; HEIGHT: 0px; margin-left: -150px; margin-top: 40px">
            <OBJECT height=200 width=1060>
                <embed src="../flash/flash.swf" width="1060" height="120"
                       quality="high" 
                       type="application/x-shockwave-flash" menu="false"
                       wmode="transparent">
                </embed>
            </OBJECT>
        </DIV>
        <%--<span id="digitalclock" class="clock"></span>--%>
    </div>
    <div id = "menu" align="center">
        <ul>
            <li><a href="../HomepageController?action=view&actor=All"> Trang chủ</a></li>
            <li><a href="../ServHompage?action=view"> Giới thiệu</a></li>
            <li><a href="../ServHompage?action=view"> Công Nghệ Phần Mềm</a></li>
            <li><a href="../ServHompage?action=view"> Khoa Học Máy Tính</a></li>
            <li><a href="GiangVien/GVStart.jsp"> Giảng viên(Test)</a></li>
            <li><a href="PDT/PDTStart.jsp"> PĐT (Test)</a></li>
            <li><a href="SinhVien/SVStart.jsp"> Sinh viên(test)</a></li>
            <li><a href="Login.jsp">Đăng nhập</a></li>
            <li><a href="jspLienHe.jsp" class="no-border">Liên Hệ</a></li>
         </ul>
    </div>
    
</body>
<script src="../javascripts/clock.js"></script>
</html>

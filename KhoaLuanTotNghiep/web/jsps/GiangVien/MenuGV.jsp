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
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">

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
    </div>
    <div id = "menu">
        <ul>
             <li><a href="../../HomepageController?actor=Lecturer"> Trang chủ |</a></li>
             <li><a href="../HomepageController?action=view"> Thời khóa biểu |</a></li>
             <li><a href="../HomepageController?action=view"> Cập nhật điểm |</a></li>
             <li><a href="../../Login?function=logout"> Đăng xuất |</a></li>
             <li><a href="../ServHompage?action=view"> Liên hệ </a></li>
        </ul>
    </div>       
</body>
</html>